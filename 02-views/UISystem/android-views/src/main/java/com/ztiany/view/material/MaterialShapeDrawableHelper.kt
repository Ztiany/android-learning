package com.ztiany.view.material

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
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
    private val context: Context,
    attrs: AttributeSet?,
    defaultStyleAttr: Int = 0,
    defaultStyleRes: Int = com.google.android.material.R.style.Widget_MaterialComponents_ShapeableImageView,
) {

    private var shapeAppearanceModel = ShapeAppearanceModel.builder(context, attrs, defaultStyleAttr, defaultStyleRes).build()

    private var drawable: MaterialShapeDrawable

    init {
        drawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(ContextCompat.getColor(context, R.color.colorPrimary))
            paintStyle = Paint.Style.FILL_AND_STROKE
            strokeWidth = 50f
            strokeColor = ContextCompat.getColorStateList(context, R.color.red)
        }
    }

}