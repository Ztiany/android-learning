package me.ztiany.okhttpanalysis

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.concurrent.thread

private const val TAG = "WSService"

/**
 *@author Ztiany
 */
class WSService : Service() {

    private val mockWebServer: MockWebServer by lazy {
        MockWebServer()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "WSService onCreate")
        thread {
            startWsServer()
        }
    }

    private fun startWsServer() {
        val mockWebServer = mockWebServer

        val serverAddress = "ws://" + mockWebServer.hostName + ":" + mockWebServer.port + "/"

        Log.d(TAG, "server start success, address= {$serverAddress}")

        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(

                object : WebSocketListener() {

                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        webSocket.send("hello, I am  WebServer !")
                        Log.d(TAG, "connect client, handle it by $webSocket")
                    }

                    override fun onMessage(webSocket: WebSocket?, text: String?) {
                        Log.d(TAG, "收到客户端消息={$text}")
                    }

                    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {

                    }

                    override fun onFailure(
                        webSocket: WebSocket?,
                        t: Throwable?,
                        response: Response?
                    ) {

                    }
                })
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mockWebServer.shutdown()
    }

}