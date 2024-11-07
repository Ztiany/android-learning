package me.ztiany.wifi.ui.client

import android.annotation.SuppressLint
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ztiany.wifi.kit.arch.BaseUIFragment
import me.ztiany.wifi.kit.arch.runRepeatedlyOnLifecycle
import me.ztiany.wifi.kit.wifi.ClientState
import me.ztiany.wifi.kit.wifi.WiFiP2PClientManager
import me.ztiany.wifi.kit.wifi.WifiP2PState
import me.ztiany.wifi.ui.ServerListAdapter
import me.ztiany.wifip2p.databinding.FragmentClientBinding

class WiFiClientFragment : BaseUIFragment<FragmentClientBinding>() {

    private val wifiP2PClientManager by lazy { WiFiP2PClientManager(requireContext()) }

    private val serverListAdapter by lazy {
        ServerListAdapter(requireContext()) {
            connectToServer(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wifiP2PClientManager.initialize()
    }

    override fun FragmentClientBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        with(rvServerList) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            addItemDecoration(MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL))
            adapter = serverListAdapter
        }

        // make textview scrollable
        tvReceived.movementMethod = ScrollingMovementMethod()
        btnSend.setOnClickListener {
            val content = etSend.text.toString()
            wifiP2PClientManager.send(content)
                .onSuccess {
                    etSend.setText("")
                    Toast.makeText(requireContext(), "Send success", Toast.LENGTH_SHORT).show()
                }
                .onFailure {
                    Toast.makeText(requireContext(), "Send failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnStartSearch.setOnClickListener {
            lifecycleScope.launch {
                wifiP2PClientManager.startSearch()
                    .onFailure {
                        Toast.makeText(requireContext(), "Search failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }.onSuccess {
                        Toast.makeText(requireContext(), "Search success", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        btnStopSearch.setOnClickListener {
            wifiP2PClientManager.stopSearch()
        }

        btnDisconnect.setOnClickListener {
            wifiP2PClientManager.disconnect()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) = invokeOnEnterTransitionEnd {
        runRepeatedlyOnLifecycle {
            launch {
                wifiP2PClientManager.wifiState.collectLatest {
                    vb.tvWifiStatus.text = "WiFiP2P State: $it"
                    vb.btnStartSearch.isEnabled = it === WifiP2PState.Enabled
                }
            }
            launch {
                wifiP2PClientManager.clientState.collectLatest {
                    if (it is ClientState.Disconnected) {
                        vb.clCommunication.visibility = View.GONE
                        vb.tvServerStatus.text = "Server State: Disconnected"
                    } else {
                        vb.clCommunication.visibility = View.VISIBLE
                        vb.tvConnectedDevice.text = "Device: ${(it as ClientState.Connected).device.deviceName}"
                        vb.tvServerStatus.text = "Server State: Connected"
                    }
                }
            }
            launch {
                wifiP2PClientManager.connectionInfo.collectLatest {
                    vb.tvConnectionInfo.text = "Connection:\n $it"
                }
            }
            launch {
                wifiP2PClientManager.wifiP2pInfo.collectLatest {
                    vb.tvWifip2pInfo.text = "WiFiP2P:\n $it"
                }
            }
            launch {
                wifiP2PClientManager.deviceInfo.collectLatest {
                    vb.tvDeviceInfo.text = "Self:\n $it"
                }
            }
            launch {
                wifiP2PClientManager.serverList.collectLatest {
                    serverListAdapter.replaceAll(it?.deviceList?.toList() ?: emptyList())
                }
            }
            launch {
                wifiP2PClientManager.groupInfo.collectLatest {
                    vb.tvGroupInfo.text = "Group:\n $it"
                }
            }
            launch {
                wifiP2PClientManager.receivedMessages.collectLatest {
                    vb.tvReceived.text = it.joinToString(separator = "\n") { content ->
                        "Received: $content"
                    }
                }
            }
        }
    }

    private fun connectToServer(device: WifiP2pDevice) {
        lifecycleScope.launch {
            showLoadingDialog()
            wifiP2PClientManager.connect(device)
                .onSuccess {
                    Toast.makeText(requireContext(), "Connect success", Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Toast.makeText(requireContext(), "Connect failed", Toast.LENGTH_SHORT).show()
                }
            dismissLoadingDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wifiP2PClientManager.destroy()
    }

}