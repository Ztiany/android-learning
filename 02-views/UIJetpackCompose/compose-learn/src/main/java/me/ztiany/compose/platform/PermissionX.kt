package me.ztiany.compose.platform

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.permissionx.guolindev.PermissionX
import me.ztiany.compose.facility.utils.findActivity

@ExperimentalPermissionsApi
@Composable
fun rememberPermissionXState(
    permission: String,
    onPermissionResult: (Boolean) -> Unit = {},
): PermissionState {
    return rememberMutablePermissionState(permission, onPermissionResult)
}

@ExperimentalPermissionsApi
@Composable
internal fun rememberMutablePermissionState(
    permission: String,
    onPermissionResult: (Boolean) -> Unit = {},
): PermissionState {
    val context = LocalContext.current

    val onResult by rememberUpdatedState(newValue = onPermissionResult)

    val permissionState = remember(permission) {
        MutablePermissionState(permission, context, context.findActivity(), onResult)
    }

    // Refresh the permission status when the lifecycle is resumed
    PermissionLifecycleCheckerEffect(permissionState)

    return permissionState
}

@ExperimentalPermissionsApi
@Stable
private class MutablePermissionState(
    override val permission: String,
    private val context: Context,
    private val activity: Activity,
    private val onResult: (Boolean) -> Unit,
) : PermissionState {

    override var status: PermissionStatus by mutableStateOf(getPermissionStatus())

    override fun launchPermissionRequest() {
        PermissionX.init(activity as FragmentActivity)
            .permissions(permission)
            .request { allGranted, grantedList, diniedList ->
                onResult(allGranted)
            }
    }

    fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    private fun getPermissionStatus(): PermissionStatus {
        val hasPermission = context.checkPermission(permission)
        return if (hasPermission) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(activity.shouldShowRationale(permission))
        }
    }

}

private fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

private fun Activity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

/**
 * Effect that updates the `hasPermission` state of a revoked [MutablePermissionState] permission
 * when the lifecycle gets called with [lifecycleEvent].
 */
@ExperimentalPermissionsApi
@Composable
private fun PermissionLifecycleCheckerEffect(
    permissionState: MutablePermissionState,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME,
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionCheckerObserver = remember(permissionState) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                // If the permission is revoked, check again.
                // We don't check if the permission was denied as that triggers a process restart.
                if (permissionState.status != PermissionStatus.Granted) {
                    permissionState.refreshPermissionStatus()
                }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}
