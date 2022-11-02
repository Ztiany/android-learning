package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.ContextThemeWrapper
import com.ztiany.view.R

/** refer [R.styleable.CodingGradientDrawable] */
internal fun parseGradientDrawableAttribute(context: Context, typedArray: TypedArray): Drawable? {
    val shapeValue = typedArray.getInt(R.styleable.CodingGradientDrawable_cgd_shape, -1)
    if (shapeValue == -1) {
        return null
    }
    val shape = when (shapeValue) {
        1 -> GradientDrawable.RECTANGLE
        2 -> GradientDrawable.OVAL
        3 -> GradientDrawable.LINE
        4 -> GradientDrawable.RING
        else -> throw IllegalArgumentException("unsupported shape value: $shapeValue")
    }

    val color = typedArray.getColor(R.styleable.CodingGradientDrawable_cgd_solid, Color.TRANSPARENT)

    val size = parseSizeAttribute(context, typedArray)
    val cornerBuilder = parseCornerAttribute(context, typedArray)
    val gradientBuilder = parseGradientAttribute(context, typedArray)
    val strokeBuilder = parseStrokeAttribute(context, typedArray)
    val paddingBuilder = parsePaddingAttribute(context, typedArray)

    return CodeGradientDrawable.Builder(context).apply {
        solidColor(CodeColorStateList.valueOf(color))
        shape(shape)
        if (size != null) {
            size(size[0], size[2], PX_UNIT)
        }
        gradientBuilder?.let { gradient(it) }
        cornerBuilder?.let { corner(it) }
        strokeBuilder?.let { stroke(it) }
        paddingBuilder?.let { padding(it) }
    }.build()
}

private fun parseGradientAttribute(context: Context, typedArray: TypedArray): Gradient.Builder? {
    val gradientResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_gradient_style, -1)
    if (gradientResourceId == -1) {
        return null
    }

    return Gradient.Builder(context).apply {

    }
}

private fun parseStrokeAttribute(context: Context, typedArray: TypedArray): Stroke.Builder? {
    val strokeResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_stroke_style, -1)
    if (strokeResourceId == -1) {
        return null
    }

    return Stroke.Builder(context).apply {

    }
}

private fun parsePaddingAttribute(context: Context, typedArray: TypedArray): Padding.Builder? {
    val paddingResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_padding_style, -1)
    if (paddingResourceId == -1) {
        return null
    }

    val paddingTypedValue = ContextThemeWrapper(context, paddingResourceId).obtainStyledAttributes(R.styleable.CodingGradientDrawablePadding)
    val left = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodingGradientDrawablePadding_cgd_padding_left, 0)
    val right = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodingGradientDrawablePadding_cgd_padding_right, 0)
    val top = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodingGradientDrawablePadding_cgd_padding_top, 0)
    val bottom = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodingGradientDrawablePadding_cgd_padding_bottom, 0)
    paddingTypedValue.recycle()

    return Padding.Builder(context).apply {
        setPadding(top, bottom, left, right, unit = PX_UNIT)
    }
}

private fun parseSizeAttribute(context: Context, typedArray: TypedArray): IntArray? {
    val sizeResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_size_style, -1)
    if (sizeResourceId == -1) {
        return null
    }

    val sizeTypedValue = ContextThemeWrapper(context, sizeResourceId).obtainStyledAttributes(R.styleable.CodingGradientDrawableSize)
    val width = sizeTypedValue.getDimensionPixelSize(R.styleable.CodingGradientDrawableSize_cgd_width, 0)
    val height = sizeTypedValue.getDimensionPixelSize(R.styleable.CodingGradientDrawableSize_cgd_height, 0)
    sizeTypedValue.recycle()

    return intArrayOf(width, height)
}

private fun parseCornerAttribute(context: Context, typedArray: TypedArray): Corner.Builder? {
    val cornerResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_corner_style, -1)
    if (cornerResourceId == -1) {
        return null
    }

    val cornerTypedValue = ContextThemeWrapper(context, cornerResourceId).obtainStyledAttributes(R.styleable.CodingGradientDrawableCorner)
    var topLeftRadius = 0F
    var topRightRadius = 0F
    var bottomLeftRadius = 0F
    var bottomRightRadius = 0F

    if (cornerTypedValue.hasValue(R.styleable.CodingGradientDrawableCorner_cgd_corner)) {
        val corner = cornerTypedValue.getDimension(R.styleable.CodingGradientDrawableCorner_cgd_corner, 0F)
        topLeftRadius = corner
        topRightRadius = corner
        bottomLeftRadius = corner
        bottomRightRadius = corner
    }

    topLeftRadius = cornerTypedValue.getDimension(R.styleable.CodingGradientDrawableCorner_cgd_corner_top_left, topLeftRadius)
    topRightRadius = cornerTypedValue.getDimension(R.styleable.CodingGradientDrawableCorner_cgd_corner_top_right, topRightRadius)
    bottomLeftRadius = cornerTypedValue.getDimension(R.styleable.CodingGradientDrawableCorner_cgd_corner_bottom_left, bottomLeftRadius)
    bottomRightRadius = cornerTypedValue.getDimension(R.styleable.CodingGradientDrawableCorner_cgd_corner_top_right, bottomRightRadius)
    cornerTypedValue.recycle()

    return Corner.Builder(context).apply {
        this.radii(
            topLeftRadius = topLeftRadius,
            topRightRadius = topRightRadius,
            bottomLeftRadius = bottomLeftRadius,
            bottomRightRadius = bottomRightRadius,
            radiusUnit = PX_UNIT
        )
    }
}

internal fun parseSelectorDrawableAttribute(typedArray: TypedArray): Drawable? {
    return null
}