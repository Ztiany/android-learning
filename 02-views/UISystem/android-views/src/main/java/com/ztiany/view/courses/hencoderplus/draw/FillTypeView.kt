package com.ztiany.view.courses.hencoderplus.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ztiany.view.courses.hencoderplus.dp

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-03 15:48
 */
class FillTypeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius = 100F.dp

    private val path = Path()

    /**为什么默认不抗锯齿，因为抗锯齿是在边缘模糊图像，会造成图像边缘变化，而 API 的设计默认就是要真实*/
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addCircle(width / 2F, height / 2F, radius, Path.Direction.CCW)
        path.addCircle(width / 2F, height / 2F + radius, radius, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.parseColor("#229af9"))
        canvas.drawPath(path, paint)
    }

}