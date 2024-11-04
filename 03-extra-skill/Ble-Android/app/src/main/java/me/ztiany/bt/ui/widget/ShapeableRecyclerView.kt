package me.ztiany.bt.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.android.base.ui.shape.EnhancedShapeable
import com.android.base.ui.shape.MaterialShapeDrawableHelper
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class ShapeableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.recyclerview.R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr), EnhancedShapeable {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr)

    init {
        mdHelper.setShapeDrawable(this)
    }

    override fun recoverShapeDrawable() {
        mdHelper.recoverShapeDrawable(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

    override fun getShapeDrawable(): MaterialShapeDrawable {
        return mdHelper.shapeDrawable
    }

}