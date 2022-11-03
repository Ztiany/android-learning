package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class DETextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr), DEView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    private val codeColorStateListHelper = CodeColorStateListHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setBackground(this)
        codeColorStateListHelper.colorStateList?.let {
            setTextColor(it)
        }
    }

    override fun updateDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
