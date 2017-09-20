package com.r2dkennobi.rosbridge.models

import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.AsyncSocket
import com.koushikdutta.async.ByteBufferList
import com.koushikdutta.async.callback.CompletedCallback
import com.koushikdutta.async.callback.DataCallback
import com.koushikdutta.async.callback.WritableCallback
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.WebSocket

/**
 * Created by Kenny Yokoyama on 6/10/17.
 */
class MockWebsocket internal constructor(private val mUri: String,
                                         private val mProtocol: String,
                                         private val mCallback:AsyncHttpClient.WebSocketConnectCallback) : WebSocket {
    private var mStringCallback: WebSocket.StringCallback? = null

    override fun send(bytes: ByteArray) = Unit

    override fun send(string: String) = Unit

    override fun send(bytes: ByteArray, offset: Int, len: Int) = Unit

    override fun ping(message: String) = Unit

    override fun pong(message: String) = Unit

    override fun setStringCallback(callback: WebSocket.StringCallback) {
        this.mStringCallback = callback
    }

    override fun getStringCallback(): WebSocket.StringCallback? = this.mStringCallback

    override fun setPingCallback(callback: WebSocket.PingCallback) = Unit

    override fun setPongCallback(callback: WebSocket.PongCallback) = Unit

    override fun getPongCallback(): WebSocket.PongCallback? = null

    override fun isBuffering(): Boolean = false

    override fun getSocket(): AsyncSocket? = null

    override fun setDataCallback(callback: DataCallback) = Unit

    override fun getDataCallback(): DataCallback? = null

    override fun isChunked(): Boolean = false

    override fun pause() = Unit

    override fun resume() = Unit

    override fun close() = Unit

    override fun isPaused(): Boolean = false

    override fun setEndCallback(callback: CompletedCallback) = Unit

    override fun getEndCallback(): CompletedCallback? = null

    override fun write(bb: ByteBufferList) = Unit

    override fun setWriteableCallback(handler: WritableCallback) = Unit

    override fun getWriteableCallback(): WritableCallback? = null

    override fun isOpen(): Boolean = true

    override fun end() = Unit

    override fun setClosedCallback(handler: CompletedCallback) = Unit

    override fun getClosedCallback(): CompletedCallback? = null

    override fun getServer(): AsyncServer? = null

    override fun charset(): String? = null
}
