package me.ztiany.wifi.kit.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import androidx.core.content.IntentCompat
import me.ztiany.wifi.kit.sys.AndroidVersion
import timber.log.Timber

abstract class WiFiP2PBridge(private val context: Context) {

    private fun newIntentFilter() = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    private val receiver by lazy {
        DirectBroadcastReceiver()
    }

    private var isRegistered = false

    @Synchronized
    fun setup() {
        if (isRegistered) {
            return
        }
        isRegistered = true

        if (AndroidVersion.atLeast(33)) {
            context.registerReceiver(receiver, newIntentFilter(), Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, newIntentFilter())
        }
    }

    @Synchronized
    fun dismantle() {
        if (!isRegistered) {
            return
        }
        isRegistered = false
        context.unregisterReceiver(receiver)
    }

    private inner class DirectBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                // 当收到 WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION 广播时，意味着 WiFi P2P 的状态发生了变化，可能是开启了 WiFi P2P 功能，或者是关闭了 WiFi P2P 功能。
                // 只要注册了 WIFI_P2P_STATE_CHANGED_ACTION 广播，立马会收到一次广播，此时可以通过 Intent 的 getIntExtra 方法获取到当前 WiFi P2P 的状态。
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    Timber.i("WIFI_P2P_STATE_CHANGED_ACTION")
                    val enabled = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1) == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                    Timber.d("WIFI_P2P_STATE: $enabled")
                    onWiFiP2PStateChanged(intent, enabled)
                }

                /*
                当收到 WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION 广播时，意味着 Wifi P2P 的连接状态发生了变化，可能是连接到了某设备，或者是与某设备断开了连接。

                        当调用 WifiP2pManager.createGroup 方法后，系统会触发 WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION 广播，此时可以通过 Intent 得
                        到 NetworkInfo, WifiP2pInfo 和 WifiP2pGroup 三个对象，分别表示网络信息，连接信息和组信息。

                        当调用 WifiP2pManager.connect 方法后，系统也会触发 WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION 广播。

                收到 WIFI_P2P_CONNECTION_CHANGED_ACTION 广播后，也可以使用 requestConnectionInfo(), requestNetworkInfo(), or requestGroupInfo() 这三个方法获
                取相关信息。
                 */
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                    // One extra EXTRA_WIFI_P2P_INFO provides the p2p connection info in the form of a WifiP2pInfo object.
                    val wifiP2pInfo = IntentCompat.getParcelableExtra(intent, WifiP2pManager.EXTRA_WIFI_P2P_INFO, WifiP2pInfo::class.java)
                    // Another extra EXTRA_NETWORK_INFO provides the network info in the form of a NetworkInfo.
                    val networkInfo = IntentCompat.getParcelableExtra(intent, WifiP2pManager.EXTRA_NETWORK_INFO, NetworkInfo::class.java)
                    // A third extra provides the details of the group.
                    val wifiP2pGroup = IntentCompat.getParcelableExtra(intent, WifiP2pManager.EXTRA_WIFI_P2P_GROUP, WifiP2pGroup::class.java)
                    Timber.i("WIFI_P2P_CONNECTION_CHANGED_ACTION")
                    Timber.d("wifiP2pInfo: $wifiP2pInfo")
                    Timber.d("networkInfo: $networkInfo")
                    Timber.d("wifiP2pGroup: $wifiP2pGroup")
                    onConnectionChanged(intent, wifiP2pGroup, wifiP2pInfo, networkInfo)
                }

                // Broadcast when you call discoverPeers(). You will usually call requestPeers() to get an updated list of peers if you handle this intent
                // in your application.
                // 当搜索结束后，系统就会触发 WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION 广播，此时就可以调用 requestPeers 方法获取设备列表信。
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                    Timber.i("WIFI_P2P_PEERS_CHANGED_ACTION")
                    onPeersChanged(intent)
                }

                // 当本设备的信息发生变化时，会触发这个广播。
                WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                    val wifiP2pDevice = IntentCompat.getParcelableExtra(intent, WifiP2pManager.EXTRA_WIFI_P2P_DEVICE, WifiP2pDevice::class.java)
                    Timber.i("WIFI_P2P_THIS_DEVICE_CHANGED_ACTION")
                    Timber.d("wifiP2pDevice: $wifiP2pDevice")
                    onThisDeviceChanged(intent, wifiP2pDevice)
                }
            }
        }
    }

    /**
     * 在这里获取到 WiFi P2P 的状态。
     */
    protected abstract fun onWiFiP2PStateChanged(intent: Intent, enabled: Boolean)

    /**
     * 在这里可以获取到连接设备的信息。即此时就可以调用 requestPeers 方法获取设备列表信。
     */
    protected open fun onPeersChanged(intent: Intent) {}

    /**
     * 在这里可以获取到连接设备的信息。
     */
    protected open fun onConnectionChanged(intent: Intent, wifiP2pGroup: WifiP2pGroup?, wifiP2pInfo: WifiP2pInfo?, networkInfo: NetworkInfo?) {}

    /**
     * 获取当前设备的信息。
     */
    protected open fun onThisDeviceChanged(intent: Intent, wifiP2pDevice: WifiP2pDevice?) {}

}