package me.ztiany.okhttpanalysis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.Exception
import kotlin.concurrent.thread

private const val TAG = "FirstFragment"

class FirstFragment : Fragment() {

    private val okHttpClient = OkHttpClient.Builder()
        .certificatePinner(
            CertificatePinner.Builder()
                .add("hencoder.com", "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")/*自签名证书*/
                .add("baidu.com", "sha256/acd")
                .build()
        )
        .authenticator(object : Authenticator {
            override fun authenticate(route: Route, response: Response): Request? {
                val header = /*getHeaderFromServer()*/"Basic xxxxx"
                return response.request().newBuilder().addHeader("Authorization", header)
                    .build()
            }
        })
        .hostnameVerifier { hostname, session ->
            hostname == "www.xxx.com"
        }
        .retryOnConnectionFailure(true)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_request.setOnClickListener {
            doRequest()
            demonstrateCertificateFail()
        }

        btn_establish.setOnClickListener {
            startWsClient()
        }
    }

    private fun doRequest() {
        okHttpClient.newCall(Request.Builder().url("https://www.baidu.com/").build())
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure() called with: call = $call, e = $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(TAG, "onResponse() called with: call = $call, response = $response")
                }
            })
    }

    private fun demonstrateCertificateFail() {
        /*
     javax.net.ssl.SSLPeerUnverifiedException: Certificate pinning failure!
      Peer certificate chain:
        sha256/+OAwmENjrBT/pI2PSOVO/I3OtHeKk7Y0PH9h8Z2z3Nw=: CN=hencoder.com
        sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=: CN=R3,O=Let's Encrypt,C=US
        sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=: CN=DST Root CA X3,O=Digital Signature Trust Co.
      Pinned certificates for hencoder.com:
        sha256/acc=
        */
        thread {
            try {
                Log.d(
                    TAG,
                    "execute response: " + okHttpClient.newCall(
                        Request.Builder().url("https://hencoder.com/").build()
                    ).execute()
                )
            } catch (e: Exception) {
                Log.d(TAG, "execute response: $e")
                e.printStackTrace()
            }
        }
    }

    private fun startWsClient() {
        requireActivity().startService(Intent(requireContext(), WSService::class.java))

        val port = et_port.text.toString().toIntOrNull() ?: return

        view?.postDelayed({

            WebSocketClient("ws://localhost:$port/", okHttpClient) {
                lifecycleScope.launch {
                    for (i in 0..1000) {
                        delay(2000)
                        try {
                            it.send("Hello from client: $i")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        },1000)
    }
}

private class WebSocketClient(
    url: String,
    okHttpClient: OkHttpClient,
    private val onSucceeded: (webSocket: WebSocket) -> Unit
) {

    private val listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response?) {
            Log.d(TAG, "Client start success")
            onSucceeded(webSocket)
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            Log.d(TAG, "WebSocketClient.onFailure: $t")
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            Log.d(TAG, "WebSocketClient.onClosing: $reason")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d(TAG, "收到服务器消息：$text")
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {

        }
    }

    private val webSocket: WebSocket = okHttpClient.newWebSocket(getRequest(url), listener)

    private fun getRequest(url: String): Request {
        return Request.Builder().get().url(url).build()
    }

}
