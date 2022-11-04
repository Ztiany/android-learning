package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.res.use
import com.ztiany.view.R

class CodeTextColorStateListHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0
) {

    private var colorStateList: CodeColorStateList? = null

    init {
        withStyleable(R.styleable.ETextView) {
            colorStateList = parseCodeColorStateListAttribute(this, buildResourceList())
        }
    }

    private fun buildResourceList() = listOf(
        ResourceInfo(R.styleable.ETextView_etv_text_color_disabled, StateEnabled, false),
        ResourceInfo(R.styleable.ETextView_etv_text_color_checked, StateChecked, true),
        ResourceInfo(R.styleable.ETextView_etv_text_color_selected, StateSelected, true),
        ResourceInfo(R.styleable.ETextView_etv_text_color_pressed, StatePressed, true),
        ResourceInfo(R.styleable.ETextView_etv_text_color_normal, null, false)
    )

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    fun setTextColor(textView: TextView) {
        colorStateList?.let {
            textView.setTextColor(it)
        }
    }

}