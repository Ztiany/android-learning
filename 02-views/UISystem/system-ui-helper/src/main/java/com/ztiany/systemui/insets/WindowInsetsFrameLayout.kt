package com.ztiany.systemui.insets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "WindowInsets"

class WindowInsetsFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            Log.d(TAG, insets.systemWindowInsets.toString())
            insets
        }
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        Log.d(TAG, "dispatchApplyWindowInsets() called with: insets = $insets")
        return super.dispatchApplyWindowInsets(insets)
    }

}