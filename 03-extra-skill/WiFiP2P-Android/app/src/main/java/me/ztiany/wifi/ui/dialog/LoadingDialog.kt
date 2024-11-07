package me.ztiany.wifi.ui.dialog

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import me.ztiany.wifip2p.databinding.DialogLoadingBinding

/**
 * @author Ztiany
 */
internal class LoadingDialog(context: Context) : AppCompatDialog(context) {

    private val vb = DialogLoadingBinding.inflate(LayoutInflater.from(context))

    init {
        setView()
    }

    private fun setView() {
        setContentView(vb.root)
    }

    fun setMessage(message: CharSequence?) {
        if (!TextUtils.isEmpty(message)) {
            vb.dialogLoadingTvTitle.text = message
        }
    }

    fun setMessage(@StringRes messageId: Int) {
        if (messageId != 0) {
            vb.dialogLoadingTvTitle.setText(messageId)
        }
    }

    override fun show() {
        showCompat {
            super@LoadingDialog.show()
        }
    }

}