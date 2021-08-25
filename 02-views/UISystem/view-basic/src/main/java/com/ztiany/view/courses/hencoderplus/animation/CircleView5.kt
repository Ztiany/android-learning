package com.ztiany.view.courses.hencoderplus.animation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ztiany.view.courses.hencoderplus.dp

class CircleView5 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var radius = 50.dp
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.color = Color.parseColor("#00796B")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

}