package com.r2dkennobi.rosbridge.models

import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.future.Future
import com.koushikdutta.async.future.SimpleFuture
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.WebSocket

/**
 * Created by Kenny Yokoyama on 6/10/17.
 *
 * Mock http client to test the ROS bridge
 */
class MockAsyncHttpClient : AsyncHttpClient(AsyncServer.getDefault()) {
    fun mockWebsocket(uri: String, protocol: String, callback: AsyncHttpClient.WebSocketConnectCallback): Future<WebSocket>? {
        val websocket = MockWebsocket(uri, protocol, callback)
        val future = SimpleFuture<WebSocket>()
        future.setComplete(websocket)
        return future
    }
}
