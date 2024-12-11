package me.ztiany.bt.ui.ble

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import me.ztiany.ble.BleServer
import me.ztiany.ble.databinding.BleFragmentServerBinding
import me.ztiany.bt.kit.arch.BaseUIFragment
import me.ztiany.bt.kit.ble.requireBluetoothAdvertisingPermission
import me.ztiany.bt.service.BleService
import timber.log.Timber

class BleServerFragment : BaseUIFragment<BleFragmentServerBinding>() {

    private var bleServer: BleServer? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bleServer = BleServer.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bleServer = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBleService()
        bindBleService()
    }

    override fun BleFragmentServerBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        btnStartGattServer.setOnClickListener {
            try {
                requireActivity().requireBluetoothAdvertisingPermission { granted ->
                    if (granted) {
                        bleServer?.startGattServer()
                    }
                }
            } catch (e: Exception) {
                Timber.d("startGattServer error: $e")
                recoverBleService()
            }
        }

        btnStartAdvertiser.setOnClickListener {
            try {
                requireActivity().requireBluetoothAdvertisingPermission { granted ->
                    if (granted) {
                        bleServer?.startAdvertising()
                    }
                }
            } catch (e: Exception) {
                Timber.d("startAdvertising error: $e")
                recoverBleService()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindBleService()
    }

    private fun recoverBleService() {
        unbindBleService()
        bindBleService()
    }

    private fun startBleService() {
        Intent(requireContext(), BleService::class.java).also { intent ->
            requireContext().startService(intent)
        }
    }

    private fun bindBleService() {
        Intent(requireContext(), BleService::class.java).also { intent ->
            requireContext().bindService(intent, serviceConnection, 0)
        }
    }

    private fun unbindBleService() {
        requireContext().unbindService(serviceConnection)
    }

}