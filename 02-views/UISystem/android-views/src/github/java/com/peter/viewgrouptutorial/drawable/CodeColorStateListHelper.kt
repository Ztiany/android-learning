package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import com.ztiany.view.R

class CodeColorStateListHelper(
    private val context: Context, private val attrs: AttributeSet?, private val defaultStyleAttr: Int = 0, private val defaultStyleRes: Int = 0
) {

    var colorStateList: CodeColorStateList? = null
        private set

    init {
        withStyleable(R.styleable.DETextView) {
            buildCodeColorStateList(this)
        }
    }

    private fun buildCodeColorStateList(typedArray: TypedArray) {
        if (typedArray.hasValue(R.styleable.DETextView_ccl_color_style)) {
            val resourceId = typedArray.getResourceId(R.styleable.DETextView_ccl_color_style, -1)
            ContextThemeWrapper(context, resourceId).obtainStyledAttributes(R.styleable.CodingColorStateList).use {
                buildCodeColorStateListByAttribute(typedArray)
            }
        } else {
            buildCodeColorStateListByAttribute(typedArray)
        }
    }

    private class ColorInfo(val color: Int, val state: State?, val add: Boolean)

    private fun buildCodeColorStateListByAttribute(typedArray: TypedArray) {
        val colorList = mutableListOf<ColorInfo>()

        addColor(colorList, R.styleable.CodingColorStateList_ccl_color_disabled, typedArray, StateEnabled, false)
        addColor(colorList, R.styleable.CodingColorStateList_ccl_color_selected, typedArray, StateSelected, true)
        addColor(colorList, R.styleable.CodingColorStateList_ccl_color_checked, typedArray, StateChecked, true)
        addColor(colorList, R.styleable.CodingColorStateList_ccl_color_pressed, typedArray, StatePressed, true)
        addColor(colorList, R.styleable.CodingColorStateList_ccl_color_normal, typedArray, null, false)

        if (colorList.isEmpty()) {
            return
        }

        colorStateList = CodeColorStateList.Builder().apply {
            colorList.forEach {
                addSelectorColorItem(SelectorColorItem.Builder().apply {
                    if (it.state != null) {
                        if (it.add) {
                            addState(it.state)
                        } else {
                            minusState(it.state)
                        }
                    }
                    color(it.color)
                })
            }
        }.build()
    }

    private fun addColor(list: MutableList<ColorInfo>, colorId: Int, typedArray: TypedArray, state: State? = null, add: Boolean = true) {
        if (typedArray.hasValue(colorId)) {
            Log.d("addColor", "addColor 2")
            val color = typedArray.getColorOrThrow(colorId)
            list.add(ColorInfo(color, state, add))
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

}