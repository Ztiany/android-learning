package com.ztiany.view.draw.matrix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 理解Matrix的变换顺序
 */
public class CanvasMatrix3View extends View {


    private Paint mPaint;
    private Rect mRect;
    int size = 200;
    private float mValue;
    private ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, 1);

    public CanvasMatrix3View(Context context) {
        this(context, null);
    }

    public CanvasMatrix3View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasMatrix3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
    }

    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    //理解顺序(倒序相对新的坐标)：
    //图形1：没有变换
    //图形2：旋转30°
    //图形3：旋转30°——>基于旋转后的左边平移到中心(重点在于此)
    //图形4：旋转30°——>平移到中心——>平移回来(相当于原地变换)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //图形1
        mRect.set(0, 0, size, size);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);

        //图形2
        int degrees = 30;
        canvas.rotate(degrees);
        mPaint.setColor(Color.BLACK);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);

        //图形3
        float a = (getMeasuredWidth() / 2) * mValue;
        float b = (getMeasuredHeight() / 2) * mValue;
        canvas.translate(a, b);
        mPaint.setColor(Color.GRAY);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);

        //图形4
        canvas.translate(-a, -b);
        mRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(0x550000FF);
        canvas.drawRect(mRect, mPaint);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mValueAnimator.start();
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(3000);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.addUpdateListener(listener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mValueAnimator.end();
    }
}
