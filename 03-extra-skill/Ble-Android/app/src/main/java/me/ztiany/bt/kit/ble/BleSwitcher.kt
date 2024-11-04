package me.ztiany.bt.kit.ble

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

enum class State {
    UNKNOWN,
    DISABLED,

    /** 拒绝打开蓝牙 */
    REJECTED,

    /** 拒绝授予蓝牙权限 */
    DENIED,
    ENABLED,
    UNAVAILABLE
}

typealias StateHandler = (State) -> Unit

internal class BluetoothSwitcher(
    private val activity: FragmentActivity,
    private val stateHandler: StateHandler? = DefaultStateHandler(activity),
) {

    private val _state = MutableStateFlow(State.UNKNOWN)

    val state get() = _state.asStateFlow()

    private val requestEnableBluetooth = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            _state.value = State.ENABLED
        } else {
            _state.value = State.REJECTED
        }
    }

    init {
        activity.lifecycleScope.launch {
            state.collectLatest {
                stateHandler?.invoke(it)
            }
        }
    }

    fun startBluetoothEnablementProcedure() {
        if (!isBluetoothSupported()) {
            _state.value = State.UNAVAILABLE
            return
        }
        if (isBluetoothEnabled()) {
            _state.value = State.ENABLED
        } else if (state.value != State.DENIED && state.value != State.REJECTED) {
            _state.value = State.DISABLED
            openBluetoothSettings()
        }
    }

    private fun isBluetoothSupported(): Boolean {
        return (activity.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter != null
    }

    private fun isBluetoothEnabled(): Boolean {
        return isBluetoothSupported() && BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
    }

    /**
     * see:
     *
     *  - [Android 12 New Bluetooth Permissions](https://stackoverflow.com/questions/67722950/android-12-new-bluetooth-permissions)
     */
    private fun openBluetoothSettings() {
        fun realOpenBluetoothSettings() {
            Timber.d("realOpenBluetoothSettings is called!")
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetooth.launch(enableBtIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionX.init(activity)
                .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        Timber.d("All permissions are granted")
                        realOpenBluetoothSettings()
                    } else {
                        Timber.d("Some permissions are denied: $deniedList")
                        _state.value = State.DENIED
                    }
                }
        } else {
            realOpenBluetoothSettings()
        }
    }

}

private class DefaultStateHandler(private val activity: FragmentActivity) : StateHandler {

    override fun invoke(state: State) {
        when (state) {
            State.UNKNOWN -> {
                Timber.d("State.UNKNOWN")
            }

            State.DISABLED -> {
                Timber.d("State.DISABLED")
            }

            State.REJECTED -> {
                Timber.d("State.REJECTED")
            }

            State.ENABLED -> {
                Timber.d("State.ENABLED")
            }

            State.UNAVAILABLE -> {
                Timber.d("State.UNAVAILABLE")
            }

            State.DENIED -> {
                Timber.d("State.DENIED")
            }
        }
    }

}