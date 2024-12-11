package me.ztiany.wifi.ui.dialog

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import me.ztiany.wifi.kit.arch.LoadingViewHost
import me.ztiany.wifip2p.R

internal class AppLoadingViewHost(private val context: Context) : LoadingViewHost {

    private var loadingDialog: LoadingDialog? = null

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean): Dialog {
        val dialog = initLoadingDialog()
        if (message.isEmpty()) {
            dialog.setMessage(R.string.please_be_waiting)
        } else {
            dialog.setMessage(message)
        }
        dialog.setCancelable(cancelable)
        if (!dialog.isShowing) {
            dialog.show()
        }
        return dialog.apply {
            loadingDialog = this
        }
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean): Dialog {
        return showLoadingDialog(context.getText(messageId), cancelable)
    }

    override fun showLoadingDialog(): Dialog {
        return showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean): Dialog {
        return showLoadingDialog("", cancelable)
    }

    override fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        throw UnsupportedOperationException("the method should be implemented by implementer of LoadingViewHost")
    }

    override fun showMessage(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(@StringRes messageId: Int) {
        showMessage(context.getText(messageId))
    }

    private fun initLoadingDialog(): LoadingDialog {
        var loadingDialog = loadingDialog
        if (loadingDialog == null) {
            val dialogInterface = createLoadingDialog(context)
            loadingDialog = dialogInterface as LoadingDialog
        }
        return loadingDialog
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingDialog?.isShowing == true
    }

    private fun createLoadingDialog(context: Context): Dialog {
        return LoadingDialog(context).apply {
            setCanceledOnTouchOutside(false)
            setMessage(R.string.please_be_waiting)
        }
    }

}