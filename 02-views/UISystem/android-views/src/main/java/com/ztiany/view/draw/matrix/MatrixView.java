package com.ztiany.view.draw.matrix;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ztiany.view.utils.UnitConverter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;

/**
 * 自定义控件其实很简单1/3：https://blog.csdn.net/aigestudio/article/details/41799811
 */
public class MatrixView extends AppCompatImageView {

    private Matrix mSaveMatrix;//保存操作之后的矩阵
    private Matrix mCurrentMatrix;//当前应用的矩阵
    private float mDownRotate;//两个手指按下时的角度

    private final static int MODE_NONE = 0;
    private final static int MODE_DRAG = 1;
    private final static int MODE_ZOOM = 2;

    private PointF mMid;//两个手指的中心点
    private PointF mStart;//按下时的位置

    private int mCurrentMode = MODE_NONE;//当前模式
    private float mPreMove;//初次按下时两个手指之间的距离，用于计算缩放比例

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSaveMatrix = new Matrix();
        mCurrentMatrix = new Matrix();
        mStart = new PointF();
        mMid = new PointF();
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!hasDrawable()) {
            return super.onTouchEvent(event);
        }

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mStart.set(event.getX(), event.getY());
                mCurrentMode = MODE_DRAG;//单个手指就是拖动状态
                mSaveMatrix.set(mCurrentMatrix);//记录上一下matrix的信息
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mCurrentMode == MODE_DRAG) {

                    mCurrentMatrix.set(mSaveMatrix);//重置到按下按下状态

                    float x = event.getX();
                    float y = event.getY();
                    float dx = x - mStart.x;
                    float dy = y - mStart.y;
                    mCurrentMatrix.postTranslate(dx, dy);//注意，这里的postTranslate是基于按下状态的

                } else if (mCurrentMode == MODE_ZOOM && event.getPointerCount() == 2) {
                    mCurrentMatrix.set(mSaveMatrix);
                    float currentMove = calcSpace(event);
                    //指尖移动距离大于10F缩放
                    if (currentMove > 10F) {
                        float scale = currentMove / mPreMove;//计算缩放比例，初次按下时的两指之间的长度/现在两指之间的长度
                        mCurrentMatrix.postScale(scale, scale, mMid.x, mMid.y);//以两指之间为中心缩放
                    }
                    float r = calcRotate(event) - mDownRotate;
                    mCurrentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mCurrentMode = MODE_NONE;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                mPreMove = calcSpace(event);
                if (mPreMove > UnitConverter.dpToPx(12)) {
                    calcMin(mMid, event);//计算两只之间的中心点
                    mCurrentMode = MODE_ZOOM;
                    mSaveMatrix.set(mCurrentMatrix);
                }
                mDownRotate = calcRotate(event);//记录两只按下时的夹角
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                break;
            }
        }
        setImageMatrix(mCurrentMatrix);
        return true;
    }

    private float calcRotate(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }

    private void calcMin(PointF mid, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        mid.set(x / 2, y / 2);
    }

    private float calcSpace(MotionEvent event) {
        return (float) Math.hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
    }

    private boolean hasDrawable() {
        return getDrawable() instanceof BitmapDrawable;
    }

}
