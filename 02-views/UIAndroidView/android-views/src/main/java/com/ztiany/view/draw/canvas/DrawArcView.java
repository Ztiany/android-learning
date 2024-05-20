package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class DrawArcView extends View {


    private Paint mPaint;
    private RectF mRectF;

    private int mCurrentAngle;
    private OnClickListener mOnClickListener;

    public DrawArcView(Context context) {
        this(context, null);
    }

    public DrawArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentAngle = 0;
                getHandler().removeCallbacksAndMessages(null);
                post(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentAngle++;
                        if (mCurrentAngle < 270) {
                            invalidate();
                            post(this);
                        }
                    }
                });
            }
        };

        setOnClickListener(mOnClickListener);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.STROKE);
        mRectF.set(0, 0, getMeasuredWidth() / 4, getMeasuredHeight() / 4);
        canvas.drawArc(mRectF, 0, mCurrentAngle, false, mPaint);

        mRectF.set(getMeasuredWidth() / 4, getMeasuredHeight() / 4, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawArc(mRectF, 0, mCurrentAngle, true, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mRectF.set(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2 + getMeasuredWidth() / 4, getMeasuredHeight() / 4);
        canvas.drawArc(mRectF, 0, mCurrentAngle, false, mPaint);

        mRectF.set(getMeasuredWidth() / 2 + getMeasuredWidth() / 4, getMeasuredHeight() / 4, getMeasuredWidth(), getMeasuredHeight() / 2);
        canvas.drawArc(mRectF, 0, mCurrentAngle, true, mPaint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mOnClickListener.onClick(DrawArcView.this);
            }
        }, 1000);
    }
}
