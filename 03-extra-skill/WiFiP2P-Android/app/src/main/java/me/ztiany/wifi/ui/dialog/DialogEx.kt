package me.ztiany.wifi.ui.dialog

import android.app.Dialog
import android.view.WindowManager
import me.ztiany.wifi.kit.sys.AndroidVersion
import me.ztiany.wifi.kit.sys.activityContext

fun Dialog.showCompat(superShow: () -> Unit) {
    //https://stackoverflow.com/questions/33644326/hiding-status-bar-while-showing-alert-dialog-android
    //https://stackoverflow.com/questions/22794049/how-do-i-maintain-the-immersive-mode-in-dialogs

    window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    superShow()

    activityContext?.let {
        window?.let { safeWindow ->
            safeWindow.decorView.systemUiVisibility = it.window.decorView.systemUiVisibility
            if (AndroidVersion.atLeast(28)) {
                val attributes = safeWindow.attributes
                attributes.layoutInDisplayCutoutMode = it.window.attributes.layoutInDisplayCutoutMode
                safeWindow.attributes = attributes
            }
        }
    }

    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}