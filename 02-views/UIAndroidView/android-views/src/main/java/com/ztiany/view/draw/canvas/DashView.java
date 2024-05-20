package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;



public class DashView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    private static final float SHORT_PERCENT = 0.01F;
    private static final float LONG_PERCENT = 0.02F;

    private float mShortAxle;
    private float mLongAxle;

    public DashView(Context context) {
        this(context, null);
    }

    public DashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mLongAxle = Math.min(w, h) * LONG_PERCENT;
        mShortAxle = Math.min(w, h) * SHORT_PERCENT;
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            canvas.translate(mWidth / 2, mHeight / 2);
            int angle = 360 / 60;
            int halfWidth = mWidth / 2;
            int temp;
            for (int i = 0; i < 60; i++) {
                temp = i % 5;
                if (temp == 0) {
                    mPaint.setStrokeWidth(UnitConverter.dpToPx(2));
                    canvas.drawLine(0, -halfWidth / 2 + mLongAxle, 0, -halfWidth / 2, mPaint);
                } else {
                    mPaint.setStrokeWidth(UnitConverter.dpToPx(1));
                    canvas.drawLine(0, -halfWidth / 2 + mShortAxle, 0, -halfWidth / 2, mPaint);
                }
                canvas.rotate(angle);

            }
            canvas.drawCircle(0, 0, halfWidth/2, mPaint);
    }
}
