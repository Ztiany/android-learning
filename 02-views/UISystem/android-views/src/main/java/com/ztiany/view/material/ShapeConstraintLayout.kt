package com.ztiany.view.material

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.shape.ShapeAppearanceModel

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), EnhancedShapeable {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

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