package com.ztiany.view.material

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.shape.*
import com.ztiany.view.R
import com.ztiany.view.SimpleLayoutFragment

/** 参考：https://blog.csdn.net/yechaoa/article/details/117339632?spm=1001.2014.3001.5501 */
class ShapeableImageViewFragment : SimpleLayoutFragment() {

    override fun getLayoutId(): Int {
        return R.layout.material_fragment_shapeable_iv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.material_tv_01).apply {
            val shapeAppearanceModel2 = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(50f)

                setAllEdges(TriangleEdgeTreatment(50f, false))
            }.build()

            val drawable2 = MaterialShapeDrawable(shapeAppearanceModel2).apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                paintStyle = Paint.Style.FILL_AND_STROKE

                strokeWidth = 50f
                strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
            }
            background = drawable2
        }

        //需要 parent.clipChildren = false
        view.findViewById<TextView>(R.id.material_tv_02).apply {
            val shapeAppearanceModel3 = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(20f)
                setRightEdge(object : TriangleEdgeTreatment(20F, false) {
                    // center 位置 ， interpolation 角的大小
                    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                        super.getEdgePath(length, 55f, interpolation, shapePath)
                    }
                })
            }.build()

            val drawable3 = MaterialShapeDrawable(shapeAppearanceModel3).apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                paintStyle = Paint.Style.FILL
            }
            background = drawable3
        }

    }

}