package com.ztiany.view.material

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.shape.ShapeAppearanceModel

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), EnhancedShapeable {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs)

    init {
        mdHelper.update(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.shapeAppearanceModel = shapeAppearanceModel
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.shapeAppearanceModel
    }

}