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
    return internalParseGradientDrawableAttribute(context, typedArray)
}

internal fun parseGradientDrawableAttributeByStyle(context: Context, resourceId: Int): Drawable? {
    val contextThemeWrapper = ContextThemeWrapper(context, resourceId)
    val gradientTypedValue = contextThemeWrapper.obtainStyledAttributes(R.styleable.CodingGradientDrawable)
    val drawable = internalParseGradientDrawableAttribute(contextThemeWrapper, gradientTypedValue)
    gradientTypedValue.recycle()
    return drawable
}

private fun internalParseGradientDrawableAttribute(
    context: Context, typedArray: TypedArray
): CodeGradientDrawable? {
    val shapeValue = typedArray.getInt(R.styleable.CodingGradientDrawable_cgd_shape, -1)
    if (shapeValue == -1) {
        return null
    }

    val size = parseSizeAttribute(context, typedArray)
    val cornerBuilder = parseCornerAttribute(context, typedArray)
    val gradientBuilder = parseGradientAttribute(context, typedArray)
    val strokeBuilder = parseStrokeAttribute(context, typedArray)
    val paddingBuilder = parsePaddingAttribute(context, typedArray)

    return CodeGradientDrawable.Builder(context).apply {
        shape(shapeValue)

        if (typedArray.hasValue(R.styleable.CodingGradientDrawable_cgd_shape_solid)) {
            solidColor(CodeColorStateList.valueOf(typedArray.getColor(R.styleable.CodingGradientDrawable_cgd_shape_solid, Color.WHITE)))
        }

        size?.let { size(it[0], it[2], PX_UNIT) }
        gradientBuilder?.let { gradient(it) }
        cornerBuilder?.let { corner(it) }
        strokeBuilder?.let { stroke(it) }
        paddingBuilder?.let { padding(it) }
    }.build()
}

private fun parseGradientAttribute(context: Context, typedArray: TypedArray): Gradient.Builder? {
    val gradientResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_shape_gradient_style, -1)
    if (gradientResourceId == -1) {
        return null
    }

    val gradientTypedValue = ContextThemeWrapper(context, gradientResourceId).obtainStyledAttributes(R.styleable.CodingGradientDrawableGradient)
    val colorList = mutableListOf<Int>()
    if (gradientTypedValue.hasValue(R.styleable.CodingGradientDrawableGradient_cgd_gradient_start_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodingGradientDrawableGradient_cgd_gradient_start_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.CodingGradientDrawableGradient_cgd_gradient_center_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodingGradientDrawableGradient_cgd_gradient_center_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.CodingGradientDrawableGradient_cgd_gradient_end_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodingGradientDrawableGradient_cgd_gradient_end_color, Color.WHITE))
    }
    val orientation = gradientTypedValue.getColor(
        R.styleable.CodingGradientDrawableGradient_cgd_gradient_orientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal
    )
    val centerX = gradientTypedValue.getFloat(R.styleable.CodingGradientDrawableGradient_cgd_gradient_center_x, 0.5F)
    val centerY = gradientTypedValue.getFloat(R.styleable.CodingGradientDrawableGradient_cgd_gradient_center_y, 0.5F)
    val radius = gradientTypedValue.getDimension(R.styleable.CodingGradientDrawableGradient_cgd_gradient_radius, 0.5F)
    val userLevel = gradientTypedValue.getBoolean(R.styleable.CodingGradientDrawableGradient_cgd_gradient_radius, false)
    val type = gradientTypedValue.getInt(R.styleable.CodingGradientDrawableGradient_cgd_gradient_type, GradientDrawable.LINEAR_GRADIENT)
    gradientTypedValue.recycle()

    return Gradient.Builder(context).apply {
        gradientType(type)
        useLevel(userLevel)
        orientation(GradientDrawable.Orientation.values()[orientation])
        gradientColors(colorList.toIntArray())
        gradientCenter(centerX, centerY)
        gradientRadius(radius, PX_UNIT)
    }
}

private fun parseStrokeAttribute(context: Context, typedArray: TypedArray): Stroke.Builder? {
    val strokeResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_shape_stroke_style, -1)
    if (strokeResourceId == -1) {
        return null
    }

    val strokeTypedValue = ContextThemeWrapper(context, strokeResourceId).obtainStyledAttributes(R.styleable.CodingGradientDrawableStroke)
    val dashWidth = strokeTypedValue.getDimension(R.styleable.CodingGradientDrawableStroke_cgd_stroke_dash_width, 0F)
    val dashGap = strokeTypedValue.getDimension(R.styleable.CodingGradientDrawableStroke_cgd_stroke_dash_gap, 0F)
    val width = strokeTypedValue.getDimension(R.styleable.CodingGradientDrawableStroke_cgd_stroke_width, 0F)
    val color = strokeTypedValue.getColor(R.styleable.CodingGradientDrawableStroke_cgd_stroke_dash_color, Color.TRANSPARENT)
    strokeTypedValue.recycle()

    return Stroke.Builder(context).apply {
        setStroke(width, CodeColorStateList.valueOf(color), dashWidth, dashGap, unit = PX_UNIT)
    }
}

private fun parsePaddingAttribute(context: Context, typedArray: TypedArray): Padding.Builder? {
    val paddingResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_shape_padding_style, -1)
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
    val sizeResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_shape_size_style, -1)
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
    val cornerResourceId = typedArray.getResourceId(R.styleable.CodingGradientDrawable_cgd_shape_corner_style, -1)
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
