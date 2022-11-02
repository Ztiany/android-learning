package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import com.ztiany.view.R

class CodeDrawableHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0
) {

    private var drawable: Drawable? = null

    init {
        withStyleable(R.styleable.CodingDrawableView) {
            buildDrawableByAttributes(this)
        }
    }

    private fun buildDrawableByAttributes(codingDrawableView: TypedArray) {
        val drawableType = codingDrawableView.getInt(R.styleable.CodingDrawableView_csv_drawable_type, -1)
        if (drawableType == 1/*gradient*/) {
            buildGradientDrawable()
        } else if (drawableType == 2/*selector*/) {
            buildSelectorDrawable()
        }
    }

    private fun buildSelectorDrawable() {
        withStyleable(R.styleable.CodingSelectorDrawable) {
            drawable = parseSelectorDrawableAttribute(this)
        }
    }

    private fun buildGradientDrawable() {
        withStyleable(R.styleable.CodingGradientDrawable) {
            drawable = parseGradientDrawableAttribute(context, this)
        }
    }

    fun setBackground(view: View) {
        view.background = drawable
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

}