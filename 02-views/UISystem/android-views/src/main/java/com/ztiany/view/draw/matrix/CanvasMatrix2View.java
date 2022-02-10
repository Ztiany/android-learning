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
public class CanvasMatrix2View extends View {


    private Paint mPaint;
    private Rect mRect;
    int size = 200;

    private float mValue;
    private ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, 30);

    public CanvasMatrix2View(Context context) {
        this(context, null);
    }

    public CanvasMatrix2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasMatrix2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
    }

    //理解顺序(倒序相对于原坐标)：
    //图形1：没有变换
    //图形2：旋转30°
    //图形3：平移到中心——>旋转30°(重点在于此)
    //图形4：平移左上角——>平移回来——>旋转30°(相当于原地变换)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //图形1
        mRect.set(0, 0, size, size);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);

        //图形2
        canvas.rotate(mValue);
        mPaint.setColor(Color.BLACK);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);

        //图形3
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mPaint.setColor(Color.GRAY);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);

        //图形4
        canvas.translate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);
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
