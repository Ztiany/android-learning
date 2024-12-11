package me.ztiany.wifi.kit.wifi

import android.net.wifi.p2p.WifiP2pDevice


fun Int.convertWiFiP2PDeviceStatus(): String {
    return when (this) {
        WifiP2pDevice.AVAILABLE -> "可用的"
        WifiP2pDevice.INVITED -> "邀请中"
        WifiP2pDevice.CONNECTED -> "已连接"
        WifiP2pDevice.FAILED -> "失败的"
        WifiP2pDevice.UNAVAILABLE -> "不可用的"
        else -> "未知"
    }
}