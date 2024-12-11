package me.ztiany.wifi.ui.server

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ztiany.wifi.kit.arch.BaseUIFragment
import me.ztiany.wifi.kit.arch.runRepeatedlyOnLifecycle
import me.ztiany.wifi.kit.wifi.ServerState
import me.ztiany.wifi.kit.wifi.WiFiP2PServerManager
import me.ztiany.wifi.kit.wifi.WifiP2PState
import me.ztiany.wifip2p.databinding.FragmentServerBinding

class WiFiServerFragment : BaseUIFragment<FragmentServerBinding>() {

    private val wifiP2PServerManager by lazy { WiFiP2PServerManager(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wifiP2PServerManager.initialize()
    }

    override fun FragmentServerBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        btnSetup.setOnClickListener {
            lifecycleScope.launch {
                wifiP2PServerManager.setup()
                    .onSuccess {
                        Toast.makeText(requireContext(), "Setup success", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "Setup failed ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        // make textview scrollable
        tvReceived.movementMethod = ScrollingMovementMethod()
        btnSend.setOnClickListener {
            val content = etSend.text.toString()
            wifiP2PServerManager.send(content)
                .onSuccess {
                    etSend.setText("")
                    Toast.makeText(requireContext(), "Send success", Toast.LENGTH_SHORT).show()
                }
                .onFailure {
                    Toast.makeText(requireContext(), "Send failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnDismantle.setOnClickListener {
            wifiP2PServerManager.dismantle()
        }

        btnStartServer.setOnClickListener {
            lifecycleScope.launch {
                wifiP2PServerManager.startServer()
                    .onSuccess {
                        Toast.makeText(requireContext(), "Server started", Toast.LENGTH_SHORT).show()
                    }.onFailure {
                        Toast.makeText(requireContext(), "Server start failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        btnStopServer.setOnClickListener {
            lifecycleScope.launch {
                wifiP2PServerManager.stopServer()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) = invokeOnEnterTransitionEnd {
        runRepeatedlyOnLifecycle {
            launch {
                wifiP2PServerManager.wifiState.collectLatest {
                    vb.tvWifiStatus.text = "WiFiP2P State: $it"
                    updateBtnState()
                }
            }
            launch {
                wifiP2PServerManager.serverState.collectLatest {
                    vb.tvServerStatus.text = "Server State: $it"
                    updateBtnState()
                }
            }
            launch {
                wifiP2PServerManager.connectionInfo.collectLatest {
                    vb.tvConnectionInfo.text = "Connection:\n $it"
                }
            }
            launch {
                wifiP2PServerManager.wifiP2pInfo.collectLatest {
                    vb.tvWifip2pInfo.text = "WiFiP2P:\n $it"
                }
            }
            launch {
                wifiP2PServerManager.deviceInfo.collectLatest {
                    vb.tvDeviceInfo.text = "Self:\n $it"
                }
            }
            launch {
                wifiP2PServerManager.groupInfo.collectLatest {
                    vb.tvGroupInfo.text = "Group:\n $it"
                }
            }
            launch {
                wifiP2PServerManager.receivedMessages.collectLatest {
                    vb.tvReceived.text = it.joinToString(separator = "\n") { content ->
                        "Received: $content"
                    }
                }
            }
        }
    }

    private fun updateBtnState() = withVB {
        val wifiP2PState = wifiP2PServerManager.wifiState.value
        val serverState = wifiP2PServerManager.serverState.value
        btnSetup.isEnabled = wifiP2PState === WifiP2PState.Enabled
        btnStartServer.isEnabled = wifiP2PState === WifiP2PState.Enabled &&
                (serverState == ServerState.Connected || serverState == ServerState.Stopped)
        btnStopServer.isEnabled = wifiP2PState === WifiP2PState.Enabled && serverState == ServerState.Running

        clCommunication.visibility = if (serverState == ServerState.Running) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        wifiP2PServerManager.destroy()
    }

}