package com.ztiany.view.courses.hencoderplus.touch


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.ztiany.view.courses.hencoderplus.utils.Utils.getAvatar
import com.ztiany.view.courses.hencoderplus.utils.dp
import kotlin.math.max
import kotlin.math.min

private val IMAGE_SIZE = 300.dp.toInt()

/**额外的可缩放比例*/
private const val EXTRA_SCALE_FACTOR = 1.5f

private const val TAG = "ScalableImageView5"

class ScalableImageView5 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var smallScale = 0f
    private var bigScale = 0f

    private val henGestureListener = HenGestureListener()
    private val gestureDetector = GestureDetectorCompat(context, henGestureListener)

    private val henScaleGestureListener = HenScaleGestureListener()
    private val scaleGestureDetector = ScaleGestureDetector(context, henScaleGestureListener)

    private val henFlingRunner = HenFlingRunner()

    private var big = false

    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)

    private val scroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //默认让图片居中
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        //计算出最大缩放只和最小缩放值
        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            //图片宽高比大于控件宽高比，即在比例上图片比控件宽。
            //最小缩放比为 view width/bitmap width
            smallScale = width / bitmap.width.toFloat()
            //最大缩放比为 view height/bitmap height + 额外缩放比
            bigScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {
            //图片宽高比小于于控件宽高比
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
        }
        Log.d(TAG, "bitmap.width = ${bitmap.width}, bitmap.height = ${bitmap.height}")
        Log.d(TAG, "width = $width, height = $height")
        Log.d(TAG, "smallScale = $smallScale, bigScale = $bigScale")
        //让图片贴边
        currentScale = smallScale
        //设置动画缩放范围
        scaleAnimator.setFloatValues(smallScale, bigScale)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*矩阵的变换，倒着看代码更容易理解【变换参考坐标系始终不变】*/
        //计算缩放比
        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        Log.d(TAG, "scaleFraction = $scaleFraction, offsetX = $offsetX, offsetY = $offsetY")
        //根据缩放比得到实际要移动的距离，并进行移动
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        //进行缩放
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    /**不让图片拖出边缘*/
    private fun fixOffsets() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    inner class HenGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
                downEvent: MotionEvent?,
                currentEvent: MotionEvent?,
                velocityX: Float,
                velocityY: Float
        ): Boolean {
            if (big) {
                scroller.fling(
                        offsetX.toInt(),
                        offsetY.toInt(),
                        velocityX.toInt(),
                        velocityY.toInt(),
                        (-(bitmap.width * bigScale - width) / 2).toInt(),
                        ((bitmap.width * bigScale - width) / 2).toInt(),
                        (-(bitmap.height * bigScale - height) / 2).toInt(),
                        ((bitmap.height * bigScale - height) / 2).toInt()
                )
                ViewCompat.postOnAnimation(this@ScalableImageView5, henFlingRunner)
            }
            return false
        }

        override fun onScroll(
                downEvent: MotionEvent?,
                currentEvent: MotionEvent?,
                distanceX: Float,
                distanceY: Float
        ): Boolean {
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffsets()
                invalidate()
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                /*
                这里修正，是为了尽量以点击位置为重心进行缩放，如何做到呢？

                目的：
                    放大前的点击图片的位置 到 中心点的距离
                     =
                    放大后的图片被点击的位置 到 中心点的距离

                计算公式：放大之后的位置减去放大的距离
                */
                offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
                fixOffsets()
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return true
        }
    }

    inner class HenScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {

        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempCurrentScale = currentScale * detector.scaleFactor
            if (tempCurrentScale < smallScale || tempCurrentScale > bigScale) {
                return false
            } else {
                currentScale *= detector.scaleFactor // 0 1; 0 无穷
                return true
            }
        }
    }

    inner class HenFlingRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScalableImageView5, this)
            }
        }
    }

}