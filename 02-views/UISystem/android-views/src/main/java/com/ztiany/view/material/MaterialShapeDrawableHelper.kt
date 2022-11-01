package com.ztiany.view.material

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.DrawableUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ztiany.view.R

/**
 * the Shapeable function in material only supports following components:
 *
 *  1. Chip
 *  2. MaterialCard
 *  3. MaterialButton
 *  4. ShapeableImageView
 *  5. FloatingActionButton
 *
 * But the MaterialShapeDrawable is pretty functional, we can use it to support more.
 *
 * references:
 *
 *  1. [Material Components——Shape的处理](https://xuyisheng.top/mdc-shape/)
 *  2. [Material Components——ShapeableImageView](https://xuyisheng.top/mdc-shape/)
 *  3. [to create rounded corners for a view without having to create a separate drawable](https://stackoverflow.com/questions/59046711/android-is-there-a-simple-way-to-create-rounded-corners-for-a-view-without-havi)
 *
 *@author Ztiany
 */
class MaterialShapeDrawableHelper(
    context: Context, attrs: AttributeSet?, defaultStyleAttr: Int = 0, defaultStyleRes: Int = 0
) {

    private var shapeAppearanceModel: ShapeAppearanceModel

    private var drawable: MaterialShapeDrawable

    init {
        val shapeTypedValue = context.obtainStyledAttributes(attrs, R.styleable.MaterialShapeDrawableView, defaultStyleAttr, defaultStyleRes)
        val bgTypedValue = context.obtainStyledAttributes(attrs, R.styleable.ViewBackgroundHelper, defaultStyleAttr, defaultStyleRes)

        val useShapeAppearance = shapeTypedValue.getBoolean(R.styleable.MaterialShapeDrawableView_msd_useShapeAppearance, true)
        shapeAppearanceModel = if (useShapeAppearance) {
            ShapeAppearanceModel.builder(context, attrs, defaultStyleAttr, defaultStyleRes).build()
        } else {
            createShapeAppearanceModelByCustomAttr(shapeTypedValue)
        }
        drawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setDrawableStyle(shapeTypedValue, bgTypedValue)
        }

        bgTypedValue.recycle()
        shapeTypedValue.recycle()
    }

    @SuppressLint("RestrictedApi")
    private fun MaterialShapeDrawable.setDrawableStyle(shapeTypedValue: TypedArray, bgTypedValue: TypedArray) {
        if (bgTypedValue.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
            tintList = bgTypedValue.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint)
            Log.d(TAG, "setDrawableStyle.tintList: $tintList")
        }

        if (bgTypedValue.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
            val tintModeValue = shapeTypedValue.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1)
            val tintMode = DrawableUtils.parseTintMode(tintModeValue, PorterDuff.Mode.SRC_IN)
            setTintMode(tintMode)
            Log.d(TAG, "setDrawableStyle.setTintMode: $tintMode")
        }

        fillColor = createFillColorListCustomAttr(shapeTypedValue)

        paintStyle = when (shapeTypedValue.getInt(R.styleable.MaterialShapeDrawableView_msd_PaintStyle, 3)) {
            1 -> Paint.Style.FILL
            2 -> Paint.Style.STROKE
            3 -> Paint.Style.FILL_AND_STROKE
            else -> throw IllegalArgumentException("unsupported style")
        }

        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeWidth)) {
            strokeWidth = shapeTypedValue.getDimension(R.styleable.MaterialShapeDrawableView_msd_strokeWidth, 0F)
            strokeColor = createStrokeListByCustomAttr(shapeTypedValue)
        }

        setDrawableShadow(shapeTypedValue)
        setDrawablePaddings(shapeTypedValue)
    }

    private fun createShapeAppearanceModelByCustomAttr(typedArray: TypedArray): ShapeAppearanceModel {
        TODO("Not implemented yet.")
    }

    private fun createFillColorListCustomAttr(typedArray: TypedArray): ColorStateList {
        val stateList = mutableListOf<IntArray>()
        val colorList = mutableListOf<Int>()

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_background_disabled)) {
            stateList.add(intArrayOf(-android.R.attr.state_enabled))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_background_disabled, Color.BLACK))
        }

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_background_selected)) {
            stateList.add(intArrayOf(android.R.attr.state_selected))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_background_selected, Color.BLACK))
        }

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_background_pressed)) {
            stateList.add(intArrayOf(android.R.attr.state_pressed))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_background_pressed, Color.BLACK))
        }

        //default color
        stateList.add(intArrayOf())
        colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_background_normal, Color.WHITE))

        return ColorStateList(stateList.toTypedArray(), colorList.toIntArray())
    }

    private fun createStrokeListByCustomAttr(typedArray: TypedArray): ColorStateList {
        val stateList = mutableListOf<IntArray>()
        val colorList = mutableListOf<Int>()

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeColor_disabled)) {
            stateList.add(intArrayOf(-android.R.attr.state_enabled))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_strokeColor_disabled, Color.BLACK))
        }

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeColor_selected)) {
            stateList.add(intArrayOf(android.R.attr.state_selected))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_strokeColor_selected, Color.BLACK))
        }

        if (typedArray.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeColor_pressed)) {
            stateList.add(intArrayOf(android.R.attr.state_pressed))
            colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_strokeColor_pressed, Color.BLACK))
        }

        //default color
        stateList.add(intArrayOf())
        colorList.add(typedArray.getColor(R.styleable.MaterialShapeDrawableView_msd_strokeColor_normal, Color.WHITE))

        return ColorStateList(stateList.toTypedArray(), colorList.toIntArray())
    }

    private fun MaterialShapeDrawable.setDrawablePaddings(shapeTypedValue: TypedArray) {
        val paddings = intArrayOf(0, 0, 0, 0)
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding)) {
            val padding = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding, 0)
            paddings[0] = padding
            paddings[1] = padding
            paddings[2] = padding
            paddings[3] = padding
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_vertical)) {
            val paddingVertical = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_vertical, 0)
            paddings[1] = paddingVertical
            paddings[3] = paddingVertical
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_horizontal)) {
            val paddingHorizontal = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_horizontal, 0)
            paddings[0] = paddingHorizontal
            paddings[2] = paddingHorizontal
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_left)) {
            val paddingTop = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_left, 0)
            paddings[0] = paddingTop
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_top)) {
            val paddingTop = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_top, 0)
            paddings[1] = paddingTop
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_right)) {
            val paddingRight = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_right, 0)
            paddings[2] = paddingRight
        }
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_padding_bottom)) {
            val paddingBottom = shapeTypedValue.getDimensionPixelOffset(R.styleable.MaterialShapeDrawableView_msd_padding_bottom, 0)
            paddings[3] = paddingBottom
        }
        setPadding(paddings[0], paddings[1], paddings[2], paddings[3])
    }

    private fun MaterialShapeDrawable.setDrawableShadow(shapeTypedValue: TypedArray) {
        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_shadow_color)) {
            setShadowColor(shapeTypedValue.getColor(R.styleable.MaterialShapeDrawableView_msd_shadow_color, Color.BLACK))
        }

        if (shapeTypedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_use_tint_color_for_shadow)) {
            setUseTintColorForShadow(shapeTypedValue.getBoolean(R.styleable.MaterialShapeDrawableView_msd_use_tint_color_for_shadow, true))
        }
    }

    fun update(target: View) {
        target.background = drawable
    }

}

private const val TAG = "MaterialShapeDrawable"