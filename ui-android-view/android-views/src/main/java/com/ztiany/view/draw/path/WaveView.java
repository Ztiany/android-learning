package com.ztiany.view.draw.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;


public class WaveView extends View {

    private float mLeftX, mRightX, mCurrentY;
    private PointF mControlPoint;
    private int mWidth, mHeight;
    private int mDistance;
    private Path mPath;
    private Paint mPaint;

    private WaterTask mWaterTask;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mPaint.setColor(Color.GREEN);
        mControlPoint = new PointF();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDistance = mHeight / 5;
        mWaterTask = new WaterTask();
        mCurrentY = mDistance;
        mLeftX = -UnitConverter.dpToPx(30);
        mRightX = mWidth + UnitConverter.dpToPx(30);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mLeftX, mCurrentY);
        mPath.quadTo(mControlPoint.x, mControlPoint.y, mRightX, mCurrentY);
        mPath.moveTo(mLeftX, mCurrentY);
        mPath.lineTo(mLeftX, mHeight);
        mPath.lineTo(mRightX, mHeight);
        mPath.lineTo(mRightX, mCurrentY);
        canvas.drawPath(mPath, mPaint);
        if (mWaterTask != null && !mWaterTask.isRunning()) {
            mWaterTask.start();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWaterTask != null) {
            mWaterTask.abort();
        }
    }

    private class WaterTask implements Runnable {
        private boolean mYAdding;
        private boolean mControlXAdding;
        private boolean mIsRunning;

        boolean isRunning() {
            return mIsRunning;
        }

        void abort() {
            mIsRunning = false;
            removeCallbacks(this);
        }

        public void start() {
            if (!mIsRunning) {
                mIsRunning = true;
                post(this);
            }
        }

        @Override
        public void run() {
            if (!mIsRunning) {
                removeCallbacks(this);
                return;
            }
            if (mYAdding) {
                mCurrentY++;
            } else {
                mCurrentY--;
            }
            if (mCurrentY > mHeight) {
                mYAdding = false;
                mCurrentY = mHeight;
            } else if (mCurrentY < 0) {
                mYAdding = true;
                mCurrentY = 0;
            }

            if (mControlXAdding) {
                mControlPoint.x += 20;
            } else {
                mControlPoint.x -= 20;
            }
            if (mControlPoint.x < -50) {
                mControlPoint.x = -50;
                mControlXAdding = true;
            } else if (mControlPoint.x > mWidth + 50) {
                mControlPoint.x = mWidth + 50;
                mControlXAdding = false;
            }
            mControlPoint.y = mCurrentY - mDistance;
            invalidate();
            post(this);
        }
    }


}
