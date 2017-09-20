package com.r2dkennobi.rosbridge.models

import com.r2dkennobi.rosbridge.RosBridge
import org.json.JSONObject

/**
 * Created by Kenny Yokoyama on 6/10/17.
 *
 * Mock ROS bridge to test the interfaces
 */
class MockRosBridge : RosBridge() {
    override fun initRosBridge(host: String, port: Int): Boolean {
        var result = false
        if (this.mServer == null) {
            val wsUrl = "ws://\$host:\$port"
            this.mServer = MockAsyncHttpClient().mockWebsocket(wsUrl, "RosBridge", mWebSocketCallback)
            this.mWebSocket = MockWebsocket(wsUrl, "RosBridge", mWebSocketCallback)
            result = true
        } else {
            cancelRosBridge()
        }
        return result
    }

    override fun sendMessage(msg: JSONObject): Boolean = this.mWebSocket != null
}
