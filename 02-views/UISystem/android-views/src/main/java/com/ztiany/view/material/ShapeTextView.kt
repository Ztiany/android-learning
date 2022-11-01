package com.ztiany.view.material

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
    defStyleRes: Int = 0,
) : MaterialTextView(context, attrs, defStyleAttr, defStyleRes), EnhancedShapeable {

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