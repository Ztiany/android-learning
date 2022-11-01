package com.ztiany.view.courses.hencoderplus.xfermod

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-03 16:42
 */
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ztiany.view.courses.hencoderplus.utils.dp

private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

/** 参考：[PorterDuff.Mode](https://developer.android.com/reference/android/graphics/PorterDuff.Mode)*/
class XfermodeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF(150f.dp, 50f.dp, 300f.dp, 200f.dp)

    /*离屏幕缓存*/
    private val circleBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)
    /*离屏幕缓存*/
    private val squareBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)

    init {
        val canvas = Canvas(circleBitmap)
        paint.color = Color.parseColor("#D81B60")
        canvas.drawOval(50f.dp, 0f.dp, 150f.dp, 100f.dp, paint)

        canvas.setBitmap(squareBitmap)
        paint.color = Color.parseColor("#2196F3")
        canvas.drawRect(0f.dp, 50f.dp, 100f.dp, 150f.dp, paint)
    }

    override fun onDraw(canvas: Canvas) {
        val count = canvas.saveLayer(bounds, null)
        canvas.drawBitmap(circleBitmap, 150f.dp, 50f.dp, paint)
        paint.xfermode = XFERMODE
        canvas.drawBitmap(squareBitmap, 150f.dp, 50f.dp, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
    }

}