package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class DrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr) {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setBackground(this)
    }

}
