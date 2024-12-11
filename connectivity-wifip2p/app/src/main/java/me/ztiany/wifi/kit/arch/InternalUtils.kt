package me.ztiany.wifi.kit.arch

import android.app.Activity
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@author Ztiany
 */
internal fun <T> T.dismissDialog(
    lastShowTime: Long,
    minimumMills: Long,
    onDismiss: (() -> Unit)?,
) where T : LoadingViewHost, T : LifecycleOwner {

    if (!isLoadingDialogShowing()) {
        onDismiss?.invoke()
        return
    }

    val dialogShowingTime = System.currentTimeMillis() - lastShowTime

    if (dialogShowingTime >= minimumMills) {
        dismissLoadingDialog()
        onDismiss?.invoke()
        return
    }

    lifecycleScope.launch {
        try {
            delay(minimumMills - dialogShowingTime)
            dismissLoadingDialog()
            onDismiss?.invoke()
        } catch (e: CancellationException) {
            onDismiss?.invoke()
        }
    }
}

internal fun Activity.getWindowBackground(): Drawable? {
    val a: TypedArray = getTheme().obtainStyledAttributes(
        intArrayOf(android.R.attr.windowBackground)
    )
    val background = a.getResourceId(0, 0)
    a.recycle()
    return ResourcesCompat.getDrawable(resources, background, theme)
}