package com.ztiany.view.material

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.shape.*
import com.ztiany.view.R
import com.ztiany.view.SimpleLayoutFragment
import com.ztiany.view.utils.UnitConverter

private const val TAG = "MaterialShapeDrawable"

class MaterialShapeDrawableFragment : SimpleLayoutFragment() {

    override fun getLayoutId() = R.layout.material_fragment_custom_shape

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.material_tv_01).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(50f)
                setAllEdges(TriangleEdgeTreatment(50f, false))
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                paintStyle = Paint.Style.FILL_AND_STROKE
                strokeWidth = 50f
                strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
            }

            background = drawable
        }

        //需要 parent.clipChildren = false
        view.findViewById<TextView>(R.id.material_tv_02).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(20f)
                setRightEdge(object : TriangleEdgeTreatment(20F, false) {
                    // center 位置 ， interpolation 角的大小
                    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                        super.getEdgePath(length, 55f, interpolation, shapePath)
                    }
                })
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                paintStyle = Paint.Style.FILL
            }
            background = drawable
        }

        view.findViewById<View>(R.id.material_view_test1).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCornerSizes(UnitConverter.dpToPx(15F))
                    .setTopLeftCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Log.d(TAG, "getCornerPath() top-left  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            shapePath.reset(0f, radius * interpolation, 180F, 180 - angle)
                            shapePath.addArc(0f, 0f, 2 * radius * interpolation, 2 * radius * interpolation, 180f, angle)
                        }
                    })
                    .setTopRightCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Log.d(TAG, "getCornerPath() top-right  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            super.getCornerPath(shapePath, angle, interpolation, radius)
                        }
                    })
                    .setBottomLeftCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Log.d(TAG, "getCornerPath() bottom-left  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            super.getCornerPath(shapePath, angle, interpolation, radius)
                        }
                    })
                    .setBottomRightCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Log.d(TAG, "getCornerPath() bottom-right  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            shapePath.reset(0f, radius * interpolation, 180F, 180 - angle)
                            shapePath.addArc(0f, 0f, 2 * radius * interpolation, 2 * radius * interpolation, 180f, angle)
                        }
                    })
                    .setLeftEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Log.d(TAG, "getEdgePath() left called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setRightEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Log.d(TAG, "getEdgePath() right called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setTopEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Log.d(TAG, "getEdgePath() top called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setBottomEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Log.d(TAG, "getEdgePath() bottom called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                paintStyle = Paint.Style.FILL
            }
            background = drawable
        }
    }

}