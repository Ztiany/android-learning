package me.ztiany.bt.kit.arch

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes

/**
 *  A host interface for displaying loading dialog and messages.
 *
 * @author Ztiany
 */
interface LoadingViewHost {

    /** Display a loading dialog with a default message. */
    fun showLoadingDialog(): Dialog

    /** Display a loading dialog. If the dialog is already showing, then the message will be reset. */
    fun showLoadingDialog(cancelable: Boolean): Dialog

    /** Display a loading dialog. If the dialog is already showing, then the message will be updated. */
    fun showLoadingDialog(message: CharSequence, cancelable: Boolean): Dialog

    /** Display a loading dialog. If the dialog is already showing, then the message will be updated. */
    fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean): Dialog

    fun dismissLoadingDialog()

    fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)? = null)

    fun isLoadingDialogShowing(): Boolean

    fun showMessage(message: CharSequence)

    fun showMessage(@StringRes messageId: Int)

    companion object {
        internal var internalLoadingViewHostFactory: LoadingViewHostFactory? = null
    }

}

typealias LoadingViewHostFactory = (context: Context) -> LoadingViewHost