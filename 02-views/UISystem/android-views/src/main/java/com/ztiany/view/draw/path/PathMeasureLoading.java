package com.ztiany.view.draw.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 16:15
 */
public class PathMeasureLoading extends View {

    private int mCenterX, mCenterY, mRadius;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mDstPath = new Path();
    private Path mCirclePath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();
    private ValueAnimator mValueAnimator;
    private float mValue;
    private float[] mPos = new float[2];

    public PathMeasureLoading(Context context) {
        super(context);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = Math.min(w, h) / 8;
        mCirclePath.reset();
        mCirclePath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
        mPathMeasure.setPath(mCirclePath, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mValueAnimator = ValueAnimator.ofFloat(0, 1.0F);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDstPath.reset();
        mDstPath.lineTo(0, 0);
        float length = mPathMeasure.getLength();
        //  start = mValue <= 0.5 ? 0 : (mValue - 0.5F) * 2 * length; 不够顺滑
        float end = length * mValue;
        //一个新的思路，从 (-0.5到0.5)的绝对值是(0到0.5)的变化梯度，从时间上来讲就是 前一半的时间和后一般的时间都是(0到0.5)
        // 如果总长为100
        // 1-50的路段  end为1-50，start为1-50
        // 50-100的路段  end为50-100，start为1-50
        float start = (float) (end - ((0.5 - Math.abs(mValue - 0.5)) * length));

        mPathMeasure.getSegment(start, end, mDstPath, true);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mDstPath, mPaint);

        //debug
        mPathMeasure.getPosTan(start, mPos, null);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(mPos[0], mPos[1], 5, mPaint);

        mPathMeasure.getPosTan(end, mPos, null);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mPos[0], mPos[1], 5, mPaint);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.end();
        }
    }
}
