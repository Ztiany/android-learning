package me.ztiany.bt.ui.ble

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ztiany.ble.databinding.BleFragmentClientBinding
import me.ztiany.bt.kit.arch.BaseUIFragment
import me.ztiany.bt.kit.arch.runRepeatedlyOnLifecycle
import me.ztiany.bt.kit.ble.BleManager
import me.ztiany.bt.kit.ble.BleState
import me.ztiany.bt.kit.ble.BtResult
import me.ztiany.bt.kit.ble.requireBluetoothScanPermission
import timber.log.Timber

@SuppressLint("MissingPermission")
class BleClientFragment : BaseUIFragment<BleFragmentClientBinding>() {

    private val deviceListPresenter by lazy {
        DeviceListPresenter(this, vb.rvBleDeviceList) {
            handleOnDeviceSelected(it)
        }
    }

    override fun BleFragmentClientBinding.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        btnBleStartScan.setOnClickListener {
            Timber.d("start scan")
            requireActivity().requireBluetoothScanPermission { granted ->
                if (granted) {
                    BleManager.startScan(60 * 1000)
                }
            }
        }

        btnBleStopScan.setOnClickListener {
            BleManager.stopScan()
        }
    }

    private fun handleOnDeviceSelected(btResult: BtResult) {
        requireActivity().requireBluetoothScanPermission { granted ->
            if (granted) {
                /*BleManager.connect(
                    requireContext(),
                    btResult.scanResult.device,
                    ConnectionSetting(
                        serviceUUID = BleService.SERVICE_UUID.toString(),
                        readUUID = BleService.CHARACTERISTIC_READ_UUID.toString(),
                        writeUUID = BleService.CHARACTERISTIC_WRITE_UUID.toString(),
                    )
                )*/
            }
        }
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) = invokeOnEnterTransitionEnd {
        runRepeatedlyOnLifecycle {
            launch {
                Timber.d("onViewPrepared launch")
                BleManager.btResults
                    .collectLatest {
                        delay(300)
                        deviceListPresenter.update(it)
                    }
            }

            launch {
                BleManager.state
                    .collectLatest {
                        vb.spinKit.visibility = if (it == BleState.SCANNING) View.VISIBLE else View.INVISIBLE
                    }
            }
        }
    }

}