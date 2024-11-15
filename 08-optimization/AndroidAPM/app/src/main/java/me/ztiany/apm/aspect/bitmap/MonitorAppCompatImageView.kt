package me.ztiany.apm.aspect.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Looper
import android.os.MessageQueue
import android.text.format.Formatter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import me.ztiany.apm.utils.activityContext
import timber.log.Timber

open class MonitorAppCompatImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr), MessageQueue.IdleHandler {

    init {
        Timber.d("MonitorImageView init with context: $context")
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        monitor()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        monitor()
    }

    private fun monitor() {
        Looper.myQueue().removeIdleHandler(this)
        Looper.myQueue().addIdleHandler(this)
    }

    override fun queueIdle(): Boolean {
        checkDrawable()
        return false
    }

    private fun checkDrawable() {
        val drawable = drawable ?: return
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val viewWidth = measuredWidth
        val viewHeight = measuredHeight
        val imageSize = calculateImageSize(drawable)
        if (imageSize > MAX_ALARM_IMAGE_SIZE) {
            Timber.w("图片大小超标 -> ${Formatter.formatFileSize(context, imageSize.toLong())}. in $activityContext.")
        }
        if (drawableWidth > viewWidth || drawableHeight > viewHeight) {
            Timber.w("图片尺寸超标 -> drawable：$drawableWidth x $drawableHeight  view：$viewWidth x $viewHeight. in $activityContext.")
        }
    }

    private fun calculateImageSize(drawable: Drawable): Int {
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap.byteCount
            else -> 0
        }
    }

    companion object {
        private const val MAX_ALARM_IMAGE_SIZE = 1024
    }

}