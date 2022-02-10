package com.ztiany.view.bitmap.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 可收缩放大的ImageView ：参考 http://www.imooc.com/learn/239
 */
public class ZoomImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean isDrag;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();

                if (getMatrixScale() < mMiddleScale) {
                    post(new AutoScaleRunnable(mMaxScale, x, y));
                } else {
                    post(new AutoScaleRunnable(mInitScale, x, y));
                }
                return super.onDoubleTap(e);
            }
        });
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private boolean mOnce;
    private Matrix mMatrix;
    private ScaleGestureDetector mScaleGestureDetector;

    private float mInitScale;
    private float mMiddleScale;
    private float mTouchSlop;
    private float mMaxScale;

    private int mLastPointerCount;//上一个多点触控的位置
    private float mLastY, mLastX;
    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    private GestureDetector mGestureDetector;

    private class AutoScaleRunnable implements Runnable {

        private float targetScale;
        private float x;
        private float y;
        private float temp;

        static final float BIG_OFFSET = 1.07F;
        static final float SMALL_OFFSET = 0.97F;

        AutoScaleRunnable(float targetScale, float x, float y) {
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getMatrixScale() > targetScale) {
                temp = SMALL_OFFSET;
            } else {
                temp = BIG_OFFSET;
            }
        }

        @Override
        public void run() {
            mMatrix.postScale(temp, temp, x, y);
            checkBorderAndCenter();
            setImageMatrix(mMatrix);
            if ((temp > 1 && getMatrixScale() < targetScale) || (temp < 1 && getMatrixScale() > targetScale)) {
                postDelayed(this, 16);
            } else {
                float scale = targetScale / getMatrixScale();
                mMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenter();
                setImageMatrix(mMatrix);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        Log.d("ZoomImageView", "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        Log.d("ZoomImageView", "onDetachedFromWindow");
    }

    @Override
    public void onGlobalLayout() {
        Log.d("ZoomImageView", "onGlobalLayout");
        if (mOnce) {
            return;
        }
        int width = getWidth();
        int height = getHeight();

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        int dw = drawable.getIntrinsicWidth();
        int dh = drawable.getIntrinsicHeight();

        Log.d("ZoomImageView", "dw:" + dw);
        Log.d("ZoomImageView", "dh:" + dh);
        Log.d("ZoomImageView", "mWidth:" + width);
        Log.d("ZoomImageView", "mHeight:" + height);

        float scale = 0;
        if (dw > width && dh < height) {
            scale = (width * 1f / (dw));
        }

        if (dw < width && dh > height) {
            scale = height * 1f / (dh);
        }

        if ((dw < width && dh < height) || (dw > width && dh > height)) {
            scale = Math.min(width * 1f / dw, height * 1f / dh);
        }
        mInitScale = scale;
        mMaxScale = mInitScale * 4;
        mMiddleScale = mInitScale * 2;
        Log.d("ZoomImageView", "mInitScale:" + mInitScale);

        int dx = width / 2 - dw / 2;
        int dy = height / 2 - dh / 2;

        mMatrix.postTranslate(dx, dy);
        mMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
        setImageMatrix(mMatrix);
        mOnce = true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float scale = getMatrixScale();
        float scaleFactor = detector.getScaleFactor();//0.0 -->

        Drawable drawable = getDrawable();
        if (drawable != null) {
            //缩放范围控制
            if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {

                if (scale * scaleFactor > mMaxScale) {
                    scaleFactor = mMaxScale / scale;
                }
                if (scale * scaleFactor < mInitScale) {
                    scaleFactor = mInitScale / scale;
                }

                mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                checkBorderAndCenter();
                setImageMatrix(mMatrix);
            }
        }
        return true;
    }

    private void checkBorderAndCenter() {
        RectF matrixRectF = getMatrixRectF();//获取的是图片在控件内的坐标
        Log.d("ZoomImageView", "matrixRectF:" + matrixRectF);
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        Log.d("ZoomImageView", "width():" + (matrixRectF.width() > width));
        Log.d("ZoomImageView", "height():" + (matrixRectF.height() > height));

        if (matrixRectF.width() > width) {
            if (matrixRectF.left > 0) {
                deltaX = -matrixRectF.left;
            }
            if (matrixRectF.right < width) {
                deltaX = width - matrixRectF.right;
            }
        }

        if (matrixRectF.height() > height) {
            if (matrixRectF.top > 0) {
                deltaY = -matrixRectF.top;
            }
            if (matrixRectF.bottom < height) {
                deltaY = height - matrixRectF.bottom;
            }
        }

        //控制居中
        if (matrixRectF.width() < width) {
            deltaX = (float) (width / 2.0 - matrixRectF.right + matrixRectF.width() / 2);
        }
        if (matrixRectF.height() < height) {
            deltaY = (float) (height / 2.0 - matrixRectF.bottom + matrixRectF.height() / 2);
        }

        mMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x = x / pointerCount;
        y = y / pointerCount;

        if (mLastPointerCount != pointerCount) {
            isDrag = false;
            mLastX = x;
            mLastY = y;
        }

        mLastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isDrag) {
                    isDrag = isMove(dx, dy);
                }
                if (isDrag) {
                    RectF matrixRectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        if (matrixRectF.width() <= getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        if (matrixRectF.height() <= getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mMatrix.postTranslate(dx, dy);
                        checkBorderWhenTrans();
                        setImageMatrix(mMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                isDrag = false;
                break;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getDrawable() != null) {
            RectF matrixRectF = getMatrixRectF();
            if ((matrixRectF.width() > getWidth()) && (matrixRectF.right - 1 > getWidth() || matrixRectF.left - 1 < 0)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 移动时检擦边界
     */
    private void checkBorderWhenTrans() {
        RectF matrixRectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        if (isCheckLeftAndRight && matrixRectF.width() > getWidth()) {
            if (matrixRectF.left > 0) {
                deltaX = -matrixRectF.left;
            }
            if (matrixRectF.right < getWidth()) {
                deltaX = (getWidth() - matrixRectF.right);
            }
        }

        if (isCheckTopAndBottom && matrixRectF.height() > getHeight()) {
            if (matrixRectF.top > 0) {
                deltaY = -matrixRectF.top;
            }
            if (matrixRectF.bottom < getHeight()) {
                deltaY = (getHeight() - matrixRectF.bottom);
            }
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    private boolean isMove(float dx, float dy) {
        //dx dy 距离中心点的距离
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    public float getMatrixScale() {
        float[] value = new float[9];
        if (mMatrix != null) {
            mMatrix.getValues(value);
        }
        //这里x。一样的
        return value[Matrix.MSCALE_X];
    }

    /**
     * 获取图片缩小后的 宽高，l t r b
     */
    public RectF getMatrixRectF() {
        RectF f = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Matrix matrix = mMatrix;
            f.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(f);
        }
        return f;
    }

}