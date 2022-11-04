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

    private fun buildDrawableByAttributes(codingDrawableView: TypedArray) {
        if (codingDrawableView.hasValue(R.styleable.CodingDrawableView_cdv_drawable_type)) {
            when (codingDrawableView.getInt(R.styleable.CodingDrawableView_cdv_drawable_type, -1)) {
                1/*gradient*/ -> buildGradientDrawable()
                2/*selector*/ -> buildSelectorDrawable()
                3/*selector*/ -> buildRippleDrawable()
                else -> throw IllegalArgumentException("Unsupported drawable type")
            }
            return
        }

        buildDrawableByAppearance(codingDrawableView)
    }

    private fun buildDrawableByAppearance(codingDrawableView: TypedArray) {
        var resourceId = codingDrawableView.getResourceId(R.styleable.CodingDrawableView_cdv_gradient_appearance, -1)
        if (resourceId != -1) {
            drawable = parseGradientDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = codingDrawableView.getResourceId(R.styleable.CodingDrawableView_cdv_selector_appearance, -1)
        if (resourceId != -1) {
            drawable = parseSelectorDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = codingDrawableView.getResourceId(R.styleable.CodingDrawableView_cdv_ripple_appearance, -1)
        if (resourceId != -1) {
            drawable = parseRippleDrawableAttributeByStyle(context, resourceId)
            return
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

    private fun buildRippleDrawable() {
        withStyleable(R.styleable.CodingRippleDrawable) {
            drawable = parseRippleDrawableAttribute(context, this)
        }
    }

}