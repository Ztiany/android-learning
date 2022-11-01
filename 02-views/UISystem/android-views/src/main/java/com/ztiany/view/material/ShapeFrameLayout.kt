package com.ztiany.view.material

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class ShapeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs)

    init {
        mdHelper.update(this)
    }

}