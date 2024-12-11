package com.ztiany.view.draw.path;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.ztiany.view.utils.UnitConverter;

public class BezierCircleTranslate extends View implements View.OnClickListener {

    private static final float C = 0.551915024494f;

    private PointF[] mControlPoints = new PointF[8];
    private PointF[] mExtremityPoints = new PointF[4];

    private Paint mPaint;
    private Path mPath;

    private AnimatorSet mAnimatorSet;

    private float mCenterX, mCenterY;
    private float mCircleRadius;
    private float mBezierDistance;

    private float mHeadAdd;
    private float mEndAdd;

    public BezierCircleTranslate(Context context) {
        this(context, null);
    }

    public BezierCircleTranslate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCircleTranslate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();

        int length = mControlPoints.length;
        for (int i = 0; i < length; i++) {
            mControlPoints[i] = new PointF();
        }
        length = mExtremityPoints.length;
        for (int i = 0; i < length; i++) {
            mExtremityPoints[i] = new PointF();
        }
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float min = Math.min(w, h) / 6F;
        mCircleRadius = min / 2;
        mCenterX = mCircleRadius;
        mCenterY = h / 2;
        mBezierDistance = mCircleRadius * C;
        setValues();
    }

    private void setValues() {

        mExtremityPoints[0].set(mCenterX + mEndAdd- mCircleRadius, mCenterY);
        mExtremityPoints[1].set(mCenterX, mCenterY - mCircleRadius );
        mExtremityPoints[2].set(mCenterX+ mHeadAdd + mCircleRadius, mCenterY);
        mExtremityPoints[3].set(mCenterX, mCenterY + mCircleRadius );

        mControlPoints[0].set(mCenterX - mCircleRadius+ mEndAdd, mCenterY - mBezierDistance);
        mControlPoints[1].set(mCenterX - mBezierDistance, mCenterY - mCircleRadius );
        mControlPoints[2].set(mCenterX + mBezierDistance, mCenterY - mCircleRadius );
        mControlPoints[3].set(mCenterX + mCircleRadius+ mHeadAdd, mCenterY - mBezierDistance );
        mControlPoints[4].set(mCenterX + mCircleRadius+ mHeadAdd, mCenterY + mBezierDistance );
        mControlPoints[5].set(mCenterX + mBezierDistance, mCenterY + mCircleRadius);
        mControlPoints[6].set(mCenterX - mBezierDistance, mCenterY + mCircleRadius);
        mControlPoints[7].set(mCenterX - mCircleRadius+ mEndAdd, mCenterY + mBezierDistance);
    }

    private void drawCircle() {
        mPath.reset();
        mPath.moveTo(mExtremityPoints[0].x, mExtremityPoints[0].y);
        int length = mExtremityPoints.length;
        int temp;
        for (int i = 0; i < length; i++) {
            temp = i + 1;
            if (temp == length) {
                temp = 0;
            }
            mPath.cubicTo(
                    mControlPoints[i * 2].x,
                    mControlPoints[i * 2].y,
                    mControlPoints[i * 2 + 1].x,
                    mControlPoints[i * 2 + 1].y,
                    mExtremityPoints[temp].x,
                    mExtremityPoints[temp].y);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(1));
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStrokeWidth(UnitConverter.dpToPx(5));
        mPaint.setColor(Color.BLUE);
        for (PointF controlPoint : mControlPoints) {
            canvas.drawPoint(controlPoint.x, controlPoint.y, mPaint);
        }
        mPaint.setColor(Color.GREEN);
        for (PointF extremityPoint : mExtremityPoints) {
            canvas.drawPoint(extremityPoint.x, extremityPoint.y, mPaint);
        }
    }


    @Override
    public void onClick(View v) {
        mCenterX = mCircleRadius;
        if (mAnimatorSet == null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCenterX, getMeasuredWidth() - mCircleRadius).setDuration(2000);
            valueAnimator.setStartDelay(1000);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCenterX = (Float) animation.getAnimatedValue();
                }
            });

            ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0, mCircleRadius * 2, 0).setDuration(3000);
            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHeadAdd = (Float) animation.getAnimatedValue();
                    setValues();
                    invalidate();
                }
            });
            ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0, -mCircleRadius * 2, 0).setDuration(1500);
            valueAnimator2.setStartDelay(1500);
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mEndAdd = (Float) animation.getAnimatedValue();
                }
            });
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(valueAnimator1).with(valueAnimator).with(valueAnimator2);

        } else {
            mAnimatorSet.cancel();
        }
        mAnimatorSet.start();
    }

}
