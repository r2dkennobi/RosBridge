package com.r2dkennobi.rosbridge

import android.util.Log

import com.koushikdutta.async.future.Future
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.WebSocket

import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Kenny Yokoyama on 3/14/17.
 *
 * The meat of ROS Bridge adapter for Android
 * @see [ROS Bridge protocol](https://github.com/RobotWebTools/rosbridge_suite/blob/develop/ROSBRIDGE_PROTOCOL.md)
 */
open class RosBridge {
    protected object Holder { val mInstance = RosBridge() }
    private val mAdvertisingTopics = ConcurrentHashMap<String, RosBridgeAdvertiserI>()
    private val mSubscribingTopics = ConcurrentHashMap<String, RosBridgeSubscriberI>()
    private val mStringCallback: WebSocket.StringCallback = WebSocket.StringCallback { s ->
        try {
            val jo = JSONObject(s)
            val topicName = jo.getString("topic")
            val dataObject = jo.getJSONObject("msg")
            if (mSubscribingTopics.containsKey(topicName)) {
                mSubscribingTopics[topicName]?.parseMessage(topicName, dataObject)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Invalid JSON handling: " + e)
            e.printStackTrace()
        }
    }
    protected val mWebSocketCallback: AsyncHttpClient.WebSocketConnectCallback = AsyncHttpClient.WebSocketConnectCallback {
        ex, webSocket ->
        when {
            ex != null -> Log.e(TAG, "ws connection failed: " + ex.message)
            webSocket == null -> Log.e(TAG, "ws connection failed. probably port issue")
            else -> {
                mWebSocket = webSocket
                webSocket.stringCallback = mStringCallback
                mAdvertisingTopics.keys.map { advertiseTopic(mAdvertisingTopics[it]!!.rosTopic) }
                mSubscribingTopics.keys
                        .flatMap { mSubscribingTopics[it]!!.rosTopics }
                        .forEach { subscribeToTopic(it) } // TODO Maybe avoid forEach
            }
        }
    }
    protected var mWebSocket: WebSocket? = null
    protected var mServer: Future<WebSocket>? = null

    /**
     * Initialize ROS bridge.
     *
     * @param host Host to initialize to
     */
    fun initRosBridge(host: String): Boolean = initRosBridge(host, DEFAULT_ROS_BRIDGE_PORT)

    /**
     * Initialize ROS bridge.
     *
     * @param host Host to initialize to
     * @param port ROS bridge port on the target host
     */
    protected open fun initRosBridge(host: String, port: Int): Boolean {
        var result = false
        Log.d(TAG, "Setting up ROS Bridge: $host:$port")
        if (this.mServer == null) {
            Log.d(TAG, "Setting up RosBridge")
            val wsUrl = "ws://$host:$port"
            this.mServer = AsyncHttpClient.getDefaultInstance().websocket(wsUrl, "RosBridge", mWebSocketCallback)
            result = true
        } else {
            Log.e(TAG, "ROS Bridge not initialized")
            cancelRosBridge()
        }
        return result
    }

    /**
     * Cancel ROS bridge. Close websocket and clear mServer.
     */
    fun cancelRosBridge() {
        this.mWebSocket?.end()
        this.mWebSocket = null
        this.mServer?.cancel(true)
        this.mServer = null
    }

    /**
     * Add advertiser to ROS bridge.
     *
     * @param cb Module that publishes message to the topic
     */
    fun addAdvertiser(cb: RosBridgeAdvertiserI): Boolean {
        var result = false
        if (!mAdvertisingTopics.containsKey(cb.rosTopic.topic)) {
            mAdvertisingTopics.put(cb.rosTopic.topic, cb)
            result = true
            if (this.mWebSocket != null) {
                advertiseTopic(cb.rosTopic)
            }
        }
        return result
    }

    /**
     * Add subscribe to ROS bridge.
     *
     * @param cb Module that subscribes to topics
     */
    fun addSubscriber(cb: RosBridgeSubscriberI): Boolean {
        var result = false
        for (rosTopic in cb.rosTopics) {
            if (!mSubscribingTopics.containsKey(rosTopic.topic)) {
                mSubscribingTopics.put(rosTopic.topic, cb)
                result = true
                if (this.mWebSocket != null) {
                    subscribeToTopic(rosTopic)
                }
            }
        }
        return result
    }

    /**
     * Remove advertiser from ROS bridge.
     *
     * @param cb Module that advertised to a topic
     */
    fun removeAdvertiser(cb: RosBridgeAdvertiserI): Boolean {
        var result = false
        if (mAdvertisingTopics.containsKey(cb.rosTopic.topic)) {
            mAdvertisingTopics.remove(cb.rosTopic.topic)
            unadvertiseTopic(cb.rosTopic)
            result = true
        }
        return result
    }

    /**
     * Remove subscriber from ROS bridge.
     *
     * @param cb Module that subscribes to topics
     */
    fun removeSubscriber(cb: RosBridgeSubscriberI): Boolean {
        var result = false
        for (rosTopic in cb.rosTopics) {
            if (mSubscribingTopics.containsKey(rosTopic.topic)) {
                mSubscribingTopics.remove(rosTopic.topic)
                result = true
                unsubscribeFromTopic(rosTopic)
            }
        }
        return result
    }

    /**
     * Publish message to a topic.
     *
     * @param topic Topic to publish message to
     * @param msg   Message to publish
     * @return True if message was published, otherwise, false
     */
    fun publishToTopic(topic: String, msg: JSONObject): Boolean {
        var result = false
        val subscription = JSONObject()
        if (this.mWebSocket != null) {
            try {
                Log.d(TAG, "Publish: " + topic)
                subscription.put("op", "publish")
                subscription.put("topic", topic)
                subscription.put("msg", msg)
                result = sendMessage(subscription)
            } catch (e: JSONException) {
                Log.e(TAG, "ROS publish failed")
            }
        }
        return result
    }

    /**
     * Advertise to ROS a user defined topic.
     *
     * @param rosTopic Topic to advertise
     * @return True if advertisement was successful, otherwise, false
     */
    private fun advertiseTopic(rosTopic: RosTopic): Boolean {
        var result = false
        val message = JSONObject()
        try {
            Log.d(TAG, "Advertise: " + rosTopic.topic)
            message.put("op", "advertise")
            message.put("topic", rosTopic.topic)
            if (rosTopic.type.isNotBlank()) {
                message.put("type", rosTopic.type)
            }
            result = sendMessage(message)
        } catch (e: JSONException) {
            Log.e(TAG, "ROS advertisement failed")
        }

        return result
    }

    /**
     * Un-advertise from a topic.
     *
     * @param rosTopic Topic to un-advertise
     * @return True if un-advertisement was successful, otherwise, false
     */
    private fun unadvertiseTopic(rosTopic: RosTopic): Boolean {
        var result = false
        val message = JSONObject()
        if (this.mWebSocket != null) {
            try {
                Log.d(TAG, "Unadvertise: " + rosTopic.topic)
                message.put("op", "unadvertise")
                message.put("topic", rosTopic.topic)
                result = sendMessage(message)
            } catch (e: JSONException) {
                Log.e(TAG, "ROS unadvertise failed")
            }
        }
        return result
    }

    /**
     * Subscribe to a topic.
     *
     * @param rosTopic Topic to subscribe to
     * @return True if subscription was successful, otherwise, false
     */
    private fun subscribeToTopic(rosTopic: RosTopic): Boolean {
        var result = false
        val subscription = JSONObject()
        if (this.mWebSocket != null) {
            try {
                Log.d(TAG, "Subscribe: " + rosTopic.topic)
                subscription.put("op", "subscribe")
                subscription.put("topic", rosTopic.topic)
                if (rosTopic.type.isNotBlank()) {
                    subscription.put("type", rosTopic.type)
                }
                Log.d(TAG, subscription.toString())
                result = sendMessage(subscription)
            } catch (e: JSONException) {
                Log.e(TAG, "ROS subscription failed")
            }
        }
        return result
    }

    /**
     * Un-subscribe from topic.
     *
     * @param rosTopic Topic to un-subscribe
     * @return True if un-subscription was successful, otherwise, false
     */
    private fun unsubscribeFromTopic(rosTopic: RosTopic): Boolean {
        var result = false
        val subscription = JSONObject()
        if (this.mWebSocket != null) {
            try {
                Log.d(TAG, "Unsubscribe: " + rosTopic.topic)
                subscription.put("op", "unsubscribe")
                subscription.put("topic", rosTopic.topic)
                result = sendMessage(subscription)
            } catch (e: JSONException) {
                Log.e(TAG, "ROS unsubscribe failed")
            }
        }
        return result
    }

    /**
     * Send ROS message over the websocket.
     *
     * @param msg ROS message to send over as a byte array
     */
    open protected fun sendMessage(msg: JSONObject): Boolean {
        if (this.mWebSocket != null && this.mWebSocket!!.isOpen) {
            this.mWebSocket!!.send(msg.toString().toByteArray())
            return true
        }
        return false
    }

    companion object {
        private val TAG = RosBridge::class.java.simpleName
        private val DEFAULT_ROS_BRIDGE_PORT = 9090
        val instance: RosBridge by lazy { Holder.mInstance }
    }
}