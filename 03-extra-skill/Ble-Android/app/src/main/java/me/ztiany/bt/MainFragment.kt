package me.ztiany.bt

import android.os.Bundle
import android.view.View
import me.ztiany.bt.kit.arch.BaseUIFragment
import me.ztiany.bt.kit.arch.commit
import me.ztiany.bt.ui.ble.BleClientFragment
import me.ztiany.bt.ui.ble.BleServerFragment
import me.ztiany.ble.databinding.FragmentMainBinding

class MainFragment : BaseUIFragment<FragmentMainBinding>() {

    override fun FragmentMainBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        btnBleClient.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                addToStack(fragment = BleClientFragment())
            }
        }

        btnBleServer.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                addToStack(fragment = BleServerFragment())
            }
        }

        btnCcb.setOnClickListener {

        }
    }

}