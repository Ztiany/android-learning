package me.ztiany.wifi.kit.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import me.ztiany.wifi.kit.wifi.transfer.server.WiFiP2PServer
import timber.log.Timber
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class WiFiP2PServerManager(private val context: Context) {

    private val coroutinesScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _wifiState = MutableStateFlow(WifiP2PState.Disabled)
    val wifiState = _wifiState.asStateFlow()

    private val _serverState = MutableStateFlow(ServerState.Disconnected)
    val serverState = _serverState.asStateFlow()

    private val _connectionInfo = MutableStateFlow<NetworkInfo?>(null)
    val connectionInfo = _connectionInfo.asStateFlow()

    private val _wifiP2pInfo = MutableStateFlow<WifiP2pInfo?>(null)
    val wifiP2pInfo = _wifiP2pInfo.asStateFlow()

    private val _groupInfo = MutableStateFlow<WifiP2pGroup?>(null)
    val groupInfo = _groupInfo.asStateFlow()

    private val _deviceInfo = MutableStateFlow<WifiP2pDevice?>(null)
    val deviceInfo = _deviceInfo.asStateFlow()

    private val wifiP2pManager by lazy {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }

    private val _receivedMessages = MutableStateFlow<List<String>>(mutableListOf())
    val receivedMessages = _receivedMessages.asStateFlow()

    private val channelListener = WifiP2pManager.ChannelListener {
        Timber.d("onChannelDisconnected")
        dismantle()
    }

    private var wifiP2pChannel: WifiP2pManager.Channel? = null

    private val wifiP2PServer by lazy { WiFiP2PServer() }

    private val wifiP2PBridge = object : WiFiP2PBridge(context) {
        override fun onWiFiP2PStateChanged(intent: Intent, enabled: Boolean) {
            _wifiState.update {
                if (enabled) {
                    WifiP2PState.Enabled
                } else {
                    WifiP2PState.Disabled
                }
            }
        }

        override fun onConnectionChanged(intent: Intent, wifiP2pGroup: WifiP2pGroup?, wifiP2pInfo: WifiP2pInfo?, networkInfo: NetworkInfo?) {
            _connectionInfo.update { networkInfo }
            _groupInfo.update { wifiP2pGroup }
            _wifiP2pInfo.update { wifiP2pInfo }
        }

        override fun onThisDeviceChanged(intent: Intent, wifiP2pDevice: WifiP2pDevice?) {
            _deviceInfo.update { wifiP2pDevice }
        }
    }

    fun initialize() {
        wifiP2PBridge.setup()
    }

    suspend fun setup(): Result<Int> {
        Timber.d("setup")
        if (wifiState.value === WifiP2PState.Disabled) {
            return Result.failure(Exception("WiFiP2P is disabled"))
        }
        return createGroup()
    }

    private suspend fun createGroup(): Result<Int> {
        Timber.d("createGroup")
        if (serverState.value === ServerState.Connected) {
            Timber.d("createGroup, already connected")
            return Result.success(0)
        }

        initChannel()

        return suspendCancellableCoroutine { continuation ->
            // Creates a peer-to-peer group with the current device as the group owner.
            wifiP2pManager.createGroup(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Timber.d("createGroup onSuccess")
                    if (wifiP2pChannel != null) {
                        Timber.d("request for GroupInfo")
                        // Requests peer-to-peer group information.
                        wifiP2pManager.requestGroupInfo(wifiP2pChannel) { group ->
                            _groupInfo.update { group }
                        }
                        _serverState.update { ServerState.Connected }
                        continuation.resume(Result.success(0))
                    } else {
                        Timber.d("createGroup onSuccess, but wifiP2pChannel is null")
                        continuation.resume(Result.failure(Exception("createGroup onSuccess, but wifiP2pChannel is null")))
                    }
                }

                override fun onFailure(reason: Int) {
                    Timber.d("createGroup onFailure: $reason")
                    _groupInfo.update { null }
                    _serverState.update { ServerState.Disconnected }
                    continuation.resume(Result.failure(Exception("createGroup onFailure: $reason")))
                }
            })
        }
    }

    private fun initChannel() {
        if (wifiP2pChannel == null) {
            Timber.d("initChannel")
            // Registers the application with the Wi-Fi framework. Call this before calling any other Wi-Fi P2P method.
            // This method returns a WifiP2pManager.Channel, which is used to connect your application to the Wi-Fi P2P framework.
            wifiP2pChannel = wifiP2pManager.initialize(context, context.mainLooper, channelListener)
        } else {
            Timber.d("initChannel, already initialized")
        }
    }

    suspend fun startServer(): Result<Int> {
        if (wifiP2pChannel == null) {
            return Result.failure(Exception("wifiP2pChannel is null"))
        }
        return wifiP2PServer.start().onSuccess {
            _serverState.update { ServerState.Running }
            wifiP2PServer.onMessageListener = { message ->
                _receivedMessages.update { it + message }
            }
        }
    }

    suspend fun stopServer() {
        wifiP2PServer.stop()
        wifiP2PServer.onMessageListener = null
        _serverState.compareAndSet(ServerState.Running, ServerState.Stopped)
    }

    fun dismantle() {
        wifiP2pChannel = null

        _wifiP2pInfo.update { null }
        _deviceInfo.update { null }
        _connectionInfo.update { null }
        _groupInfo.update { null }

        _serverState.update { ServerState.Disconnected }

        removeGroup()

        coroutinesScope.launch {
            stopServer()
        }
    }

    private fun removeGroup() {
        if (wifiP2pChannel == null) {
            _groupInfo.update { null }
            return
        }

        // Removes the current peer-to-peer group.
        wifiP2pManager.requestGroupInfo(wifiP2pChannel) { group ->
            if (group == null) {
                _groupInfo.update { null }
                Timber.d("removeGroup group is null")
            } else {
                wifiP2pManager.removeGroup(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        Timber.d("removeGroup onSuccess")
                        _groupInfo.update { null }
                    }

                    override fun onFailure(reason: Int) {
                        Timber.d("removeGroup onFailure: $reason")
                        _groupInfo.update { null }
                    }
                })
            }
        }
    }

    fun send(message: String): Result<Int> {
        return wifiP2PServer.send(message)
    }

    fun destroy() {
        dismantle()
        wifiP2PBridge.dismantle()
    }

}