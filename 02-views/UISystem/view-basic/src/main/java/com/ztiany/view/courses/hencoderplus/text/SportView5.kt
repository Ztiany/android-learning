package com.ztiany.view.courses.hencoderplus.text


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.ztiany.view.R
import com.ztiany.view.courses.hencoderplus.dp


private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp

class SportView5 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100.dp
        /*
        参考
            1. https://developer.android.com/guide/topics/ui/look-and-feel/fonts-in-xml?hl=zh-cn【Android 8.0（API 级别 26）引入了“XML 中的字体”这项新功能】
            2. https://developer.android.com/guide/topics/resources/font-resource?hl=zh-cn
         */
        typeface = ResourcesCompat.getFont(context, R.font.font)
        textAlign = Paint.Align.CENTER
    }

    private val bounds = Rect()

    private val fontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制环
        paint.style = Paint.Style.STROKE
        paint.color = CIRCLE_COLOR
        paint.strokeWidth = RING_WIDTH
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        // 绘制进度条
        paint.color = HIGHLIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, -90f, 225f, false, paint)

        // 绘制文字
        paint.textSize = 100.dp
        paint.style = Paint.Style.FILL
        paint.getFontMetrics(fontMetrics)
        canvas.drawText("aaaa", width / 2f, height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f, paint)

        // 绘制文字2
        paint.textSize = 150.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getFontMetrics(fontMetrics)
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        canvas.drawText("abab", -bounds.left.toFloat(), -bounds.top.toFloat(), paint)

        // 绘制文字3
        paint.textSize = 15.dp
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        canvas.drawText("abab", -bounds.left.toFloat(), -bounds.top.toFloat(), paint)
    }

}