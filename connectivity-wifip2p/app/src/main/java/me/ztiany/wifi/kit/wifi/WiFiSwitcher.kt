package me.ztiany.wifi.kit.wifi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

fun Fragment.requireNecessaryPermission(block: (granted: Boolean) -> Unit) {
    val permissions = buildList {
        add(Manifest.permission.ACCESS_NETWORK_STATE)
        add(Manifest.permission.CHANGE_NETWORK_STATE)
        add(Manifest.permission.ACCESS_WIFI_STATE)
        add(Manifest.permission.CHANGE_WIFI_STATE)

        // 理论上，这两个权限在 Build.VERSION_CODES.TIRAMISU 之后是不需要的，但是
        // 测试发现某些手机在 Android 12 上仍然需要这两个权限才能正常使用 WiFi Direct
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        add(Manifest.permission.ACCESS_FINE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }
    }

    PermissionX.init(this)
        .permissions(*permissions.toTypedArray())
        .request { allGranted, _, _ ->
            block(allGranted)
        }
}

fun FragmentActivity.isNecessaryPermissionGranted(): Boolean {
    return PermissionX.isGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
            PermissionX.isGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)
}

enum class State {
    UNKNOWN,
    REJECTED,
    DISABLED,
    ENABLED,
}

typealias StateHandler = (State) -> Unit

internal class WiFiSwitcher(private val fragment: Fragment) {

    private var stateHandler: StateHandler = DefaultStateHandler(fragment) {
        startWiFiEnablementProcedure()
    }

    private val _state = MutableStateFlow(State.UNKNOWN)
    val state get() = _state.asStateFlow()

    private val requestEnableBluetooth = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (isWiFiEnabled()) {
            _state.value = State.ENABLED
        } else {
            _state.value = State.DISABLED
        }
    }

    init {
        fragment.lifecycleScope.launch {
            state.collectLatest {
                stateHandler.invoke(it)
            }
        }
    }

    fun startWiFiEnablementProcedure() {
        fragment.requireNecessaryPermission { granted ->
            if (granted) {
                if (isWiFiEnabled()) {
                    _state.value = State.ENABLED
                } else {
                    requestEnableBluetooth.launch(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            } else {
                _state.value = State.REJECTED
            }
        }
    }

    private fun isWiFiEnabled(): Boolean {
        return (fragment.requireContext().getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.isWifiEnabled == true
    }

}

private class DefaultStateHandler(
    private val fragment: Fragment,
    private val continueAction: () -> Unit,
) : StateHandler {

    override fun invoke(state: State) {
        when (state) {
            State.UNKNOWN -> {
                Timber.d("State.UNKNOWN")
            }

            State.DISABLED -> {
                AlertDialog.Builder(fragment.requireContext())
                    .setTitle("WiFi 未打开")
                    .setMessage("请打开 WiFi 以继续使用应用")
                    .setPositiveButton("打开 WiFi") { _, _ ->
                        continueAction.invoke()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        fragment.activity?.finish()
                    }
                    .setCancelable(false)
                    .show()
            }

            State.REJECTED -> {
                AlertDialog.Builder(fragment.requireContext())
                    .setTitle("权限被拒绝")
                    .setMessage("请授予应用所需权限以继续使用应用")
                    .setPositiveButton("好的") { _, _ ->
                        continueAction.invoke()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        fragment.activity?.finish()
                    }
                    .setCancelable(false)
                    .show()
            }

            State.ENABLED -> {
                Timber.d("State.ENABLED")
            }
        }
    }

}