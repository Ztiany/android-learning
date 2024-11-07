package me.ztiany.wifi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import me.ztiany.wifi.kit.arch.BaseUIFragment
import me.ztiany.wifi.kit.arch.commit
import me.ztiany.wifi.kit.wifi.State
import me.ztiany.wifi.kit.wifi.WiFiSwitcher
import me.ztiany.wifi.ui.client.WiFiClientFragment
import me.ztiany.wifi.ui.server.WiFiServerFragment
import me.ztiany.wifip2p.databinding.FragmentMainBinding

class MainFragment : BaseUIFragment<FragmentMainBinding>() {

    private val wifiSwitcher = WiFiSwitcher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wifiSwitcher.startWiFiEnablementProcedure()
    }

    override fun FragmentMainBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        btnClient.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                val value = wifiSwitcher.state.value
                if (value != State.ENABLED) {
                    Toast.makeText(requireContext(), "WiFi is not enabled: $value", Toast.LENGTH_SHORT).show()
                } else {
                    addToStack(fragment = WiFiClientFragment())
                }
            }
        }

        btnServer.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                val value = wifiSwitcher.state.value
                if (value != State.ENABLED) {
                    Toast.makeText(requireContext(), "WiFi is not enabled: $value", Toast.LENGTH_SHORT).show()
                } else {
                    addToStack(fragment = WiFiServerFragment())
                }
            }
        }
    }

}