package com.com.android.base.ui.shape

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.google.android.material.shape.ShapeAppearanceModel

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), EnhancedShapeable {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs)

    init {
        mdHelper.update(this)
    }

    override fun updateShapeDrawable() {
        mdHelper.update(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

}