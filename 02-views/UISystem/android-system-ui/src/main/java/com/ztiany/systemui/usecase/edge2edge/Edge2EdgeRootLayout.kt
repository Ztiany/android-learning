package com.ztiany.systemui.usecase.edge2edge

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import timber.log.Timber

class Edge2EdgeRootLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            Timber.d(insets.systemWindowInsets.toString())
            insets
        }
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        Timber.d("dispatchApplyWindowInsets() called with: insets = $insets")
        return super.dispatchApplyWindowInsets(insets)
    }

}