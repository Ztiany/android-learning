package me.ztiany.bt.kit.ble

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import me.ztiany.bt.kit.sys.AndroidVersion

/**
 * see:
 *
 *  - [Bluetooth FINE_LOCATION and COARSE_LOCATION Permissions on Android 11 or lower](https://stackoverflow.com/questions/75684547/bluetooth-fine-location-and-coarse-location-permissions-on-android-11-or-lower)
 */
fun FragmentActivity.requireBluetoothScanPermission(block: (granted: Boolean) -> Unit) {
    if (AndroidVersion.before(23)) {
        block(true)
        return
    }
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    if (AndroidVersion.atLeast(31)) {
        permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
    }

    PermissionX.init(this)
        .permissions(*permissions.toTypedArray())
        .request { allGranted, _, _ ->
            block(allGranted)
        }
}

fun FragmentActivity.requireBluetoothAdvertisingPermission(block: (granted: Boolean) -> Unit) {
    if (AndroidVersion.before(23)) {
        block(true)
        return
    }
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    if (AndroidVersion.atLeast(31)) {
        permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
    }
    PermissionX.init(this)
        .permissions(*permissions.toTypedArray())
        .request { allGranted, _, _ ->
            block(allGranted)
        }
}