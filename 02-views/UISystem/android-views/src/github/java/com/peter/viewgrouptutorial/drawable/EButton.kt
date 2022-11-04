package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class EButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr), CodeDrawableView, CodeTextColorView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    private val codeTextColorStateListHelper = CodeTextColorStateListHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setBackground(this)
        codeTextColorStateListHelper.setTextColor(this)
    }

    override fun updateDrawable() {
        codeDrawableHelper.setBackground(this)
    }

    override fun updateTextColor() {
        codeTextColorStateListHelper.setTextColor(this)
    }

}
