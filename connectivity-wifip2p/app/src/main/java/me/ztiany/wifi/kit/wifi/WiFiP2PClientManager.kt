package me.ztiany.wifi.kit.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import me.ztiany.wifi.kit.wifi.transfer.client.WiFiP2PClient
import timber.log.Timber
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class WiFiP2PClientManager(private val context: Context) {

    private val coroutinesScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _wifiState = MutableStateFlow(WifiP2PState.Disabled)
    val wifiState = _wifiState.asStateFlow()

    private val _clientState = MutableStateFlow<ClientState>(ClientState.Disconnected)
    val clientState = _clientState.asStateFlow()

    private val _connectionInfo = MutableStateFlow<NetworkInfo?>(null)
    val connectionInfo = _connectionInfo.asStateFlow()

    private val _wifiP2pInfo = MutableStateFlow<WifiP2pInfo?>(null)
    val wifiP2pInfo = _wifiP2pInfo.asStateFlow()

    private val _deviceInfo = MutableStateFlow<WifiP2pDevice?>(null)
    val deviceInfo = _deviceInfo.asStateFlow()

    private val _serverList = MutableStateFlow<WifiP2pDeviceList?>(null)
    val serverList = _serverList.asStateFlow()

    private val _groupInfo = MutableStateFlow<WifiP2pGroup?>(null)
    val groupInfo = _groupInfo.asStateFlow()

    private val _receivedMessages = MutableStateFlow<List<String>>(emptyList())
    val receivedMessages = _receivedMessages.asStateFlow()

    private val wifiP2pManager by lazy {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }

    private val channelListener = WifiP2pManager.ChannelListener {
        Timber.d("onChannelDisconnected")
        onChannelDisconnected()
    }

    private var wifiP2pChannel: WifiP2pManager.Channel? = null

    private val wifiP2PClient by lazy { WiFiP2PClient() }

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
            _wifiP2pInfo.update { wifiP2pInfo }
            _groupInfo.update { wifiP2pGroup }
        }

        override fun onThisDeviceChanged(intent: Intent, wifiP2pDevice: WifiP2pDevice?) {
            _deviceInfo.update { wifiP2pDevice }
        }

        override fun onPeersChanged(intent: Intent) {
            refreshPeers()
        }
    }

    private fun refreshPeers() {
        if (wifiP2pChannel == null) {
            Timber.d("onPeersChanged, but wifiP2pChannel is null.")
            _serverList.update { null }
            return
        }
        // Requests the current list of discovered peers.
        wifiP2pManager.requestPeers(wifiP2pChannel) { peers ->
            Timber.d("requestPeers result: ${peers.deviceList}")
            _serverList.update { peers }
        }
    }

    fun initialize() {
        Timber.d("initialize")
        wifiP2PBridge.setup()
    }

    private fun initChannel() {
        if (wifiP2pChannel == null) {
            Timber.d("initChannel")
            wifiP2pChannel = wifiP2pManager.initialize(context, context.mainLooper, channelListener)
        }
    }

    suspend fun startSearch(): Result<Int> {
        if (wifiState.value === WifiP2PState.Disabled) {
            return Result.failure(Exception("WiFiP2P is disabled"))
        }

        initChannel()

        return suspendCancellableCoroutine { continuation ->
            // Initiates peer discovery.
            wifiP2pManager.discoverPeers(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    continuation.resume(Result.success(0))
                }

                override fun onFailure(reasonCode: Int) {
                    continuation.resume(Result.failure(Exception("startSearch failed: $reasonCode")))
                }
            })
        }
    }

    fun stopSearch() {
        if (wifiP2pChannel == null) {
            Timber.d("stopSearch is called, but wifiP2pChannel is null.")
            return
        }
        wifiP2pManager.stopPeerDiscovery(wifiP2pChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Timber.d("stopSearch onSuccess.")
            }

            override fun onFailure(reasonCode: Int) {
                Timber.e("stopSearch onFailure: $reasonCode")
            }
        })
    }

    suspend fun connect(wifiP2pDevice: WifiP2pDevice): Result<Int> {
        if (wifiP2pChannel == null) {
            return Result.failure(Exception("wifiP2pChannel is null"))
        }

        val wifiP2pConfig = WifiP2pConfig()
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress
        wifiP2pConfig.wps.setup = WpsInfo.PBC

        val connected = suspendCancellableCoroutine<Result<Int>> { continuation ->
            // Starts a peer-to-peer connection with a device with the specified configuration.
            wifiP2pManager.connect(wifiP2pChannel, wifiP2pConfig, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    continuation.resume(Result.success(0))
                }

                override fun onFailure(reason: Int) {
                    continuation.resume(Result.failure(Exception("connect failed: $reason")))
                }
            })
        }

        // wait for group info to be updated.
        delay(2000)

        val address = wifiP2pInfo.value?.groupOwnerAddress?.hostAddress?:return Result.failure(Exception("wifiP2pInfo is null"))

        return if (connected.isSuccess) {
            wifiP2PClient.start(address).onSuccess {
                _clientState.update { ClientState.Connected(wifiP2pDevice) }
                wifiP2PClient.onMessageListener = {
                    _receivedMessages.update { messages ->
                        messages + it
                    }
                }
            }
        } else {
            connected
        }
    }

    fun disconnect() {
        coroutinesScope.launch {
            disconnectForResult()
        }
    }

    private suspend fun disconnectForResult(): Result<Int> {
        if (wifiP2pChannel == null) {
            Timber.d("disconnect is called, but wifiP2pChannel is null.")
            return Result.success(0)
        }

        val disconnected = suspendCancellableCoroutine<Result<Int>> { continuation ->
            // 	Cancels any ongoing peer-to-peer group negotiation.
            wifiP2pManager.cancelConnect(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Timber.d("disconnect onSuccess.")
                    continuation.resume(Result.success(0))
                }

                override fun onFailure(reason: Int) {
                    Timber.e("disconnect failed: $reason")
                    continuation.resume(Result.failure(Exception("disconnect failed: $reason")))
                }
            })
        }

        // 不移除组，会导致无法再次连接
        wifiP2pManager.removeGroup(wifiP2pChannel, null)
        refreshPeers()

        // 断开 TCP 连接
        _clientState.update { ClientState.Disconnected }
        wifiP2PClient.stop()
        wifiP2PClient.onMessageListener = null

        return disconnected
    }

    fun send(message: String): Result<Int> {
        return wifiP2PClient.send(message)
    }

    private fun onChannelDisconnected() {
        wifiP2pChannel = null

        _connectionInfo.update { null }
        _wifiP2pInfo.update { null }
        _deviceInfo.update { null }
        _serverList.update { null }
        _groupInfo.update { null }

        _clientState.update { ClientState.Disconnected }

        disconnect()
    }

    fun destroy() {
        onChannelDisconnected()
        wifiP2PBridge.dismantle()
    }

}