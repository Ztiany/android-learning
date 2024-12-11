package me.ztiany.wifi.kit.wifi

import android.net.wifi.p2p.WifiP2pDevice

enum class WifiP2PState {
    Disabled,
    Enabled
}

enum class ServerState {
    Connected,
    Running,
    Stopped,
    Disconnected
}

sealed interface ClientState {

    data class Connected(
        val device: WifiP2pDevice,
    ) : ClientState

    data object Disconnected : ClientState
}

