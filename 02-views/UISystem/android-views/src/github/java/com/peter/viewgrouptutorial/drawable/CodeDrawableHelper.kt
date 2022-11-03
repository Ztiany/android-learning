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
        val drawableType = codingDrawableView.getInt(R.styleable.CodingDrawableView_cdv_drawable_type, -1)
        if (drawableType == 1/*gradient*/) {
            buildGradientDrawable()
            return
        }
        if (drawableType == 2/*selector*/) {
            buildSelectorDrawable()
            return
        }

        var resourceId = codingDrawableView.getResourceId(R.styleable.CodingDrawableView_cdv_gradient_appearance, -1)
        if (resourceId != -1) {
            drawable = parseGradientDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = codingDrawableView.getResourceId(R.styleable.CodingDrawableView_cdv_selector_appearance, -1)
        if (resourceId != -1) {
            drawable = parseSelectorDrawableAttributeByStyle(context, resourceId)
        }
    }

    private fun buildSelectorDrawable() {
        withStyleable(R.styleable.CodingSelectorDrawable) {
            drawable = parseSelectorDrawableAttribute(context, this)
        }
    }

    private fun buildGradientDrawable() {
        withStyleable(R.styleable.CodingGradientDrawable) {
            drawable = parseGradientDrawableAttribute(context, this)
        }
    }

    fun setBackground(view: View) {
        drawable?.let {
            view.background = it
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

}