package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout

class ELinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs), CodeDrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs)

    init {
        codeDrawableHelper.setBackground(this)
    }

    override fun updateDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
