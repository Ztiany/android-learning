package com.ztiany.view.draw.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;

public class PathMeasureAnimView extends View {

    private static final float C = 0.551915024494f;

    private PointF[] mControlPoints = new PointF[8];
    private PointF[] mExtremityPoints = new PointF[4];

    private Paint mPaint;
    private Path mPath;

    private float mPointWidth;
    private PointF mPointF;

    private PathMeasure mPathMeasure;
    private ValueAnimator mValueAnimator;
    private float[] mCurrentPoint;
    private float[] mCurrentTanPoint;


    public PathMeasureAnimView(Context context) {
        this(context, null);
    }

    public PathMeasureAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mCurrentPoint = new float[2];
        mCurrentTanPoint = new float[2];

        int length = mControlPoints.length;
        for (int i = 0; i < length; i++) {
            mControlPoints[i] = new PointF();
        }
        length = mExtremityPoints.length;
        for (int i = 0; i < length; i++) {
            mExtremityPoints[i] = new PointF();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float min = Math.min(w, h) / 1.8F;

        mPointWidth = min * 0.2F;

        float circleRadius = min / 2;
        float bezierDistance = circleRadius * C;

        mExtremityPoints[0].set(-circleRadius, 0);
        mExtremityPoints[1].set(0, -circleRadius);
        mExtremityPoints[2].set(circleRadius, 0);
        mExtremityPoints[3].set(0, circleRadius);


        mControlPoints[0].set(-circleRadius, -bezierDistance);
        mControlPoints[1].set(-bezierDistance, -circleRadius);
        mControlPoints[2].set(+bezierDistance, -circleRadius);
        mControlPoints[3].set(circleRadius, -bezierDistance);

        mControlPoints[4].set(circleRadius, bezierDistance);
        mControlPoints[5].set(bezierDistance, circleRadius);
        mControlPoints[6].set(-bezierDistance, circleRadius);
        mControlPoints[7].set(-circleRadius, bezierDistance);


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
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(1));
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(5));
        mPaint.setColor(Color.BLUE);


        for (PointF controlPoint : mControlPoints) {
            canvas.drawPoint(controlPoint.x, controlPoint.y, mPaint);
        }

        for (PointF extremityPoint : mExtremityPoints) {
            canvas.drawPoint(extremityPoint.x, extremityPoint.y, mPaint);
        }

        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(20));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(mCurrentPoint[0], mCurrentPoint[1], mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawPoint(mCurrentTanPoint[0], mCurrentTanPoint[1], mPaint);
    }


    public void startAnim() {
        mPathMeasure = new PathMeasure(mPath, true);
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        float length = mPathMeasure.getLength();
        mValueAnimator = ValueAnimator.ofFloat(0, length).setDuration(3000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(animatedValue, mCurrentPoint, mCurrentTanPoint);
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        x -= getMeasuredWidth() / 2;
        y -= getMeasuredHeight() / 2;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (PointF controlPoint : mControlPoints) {
                if (getCatchPoint(x, y, controlPoint)) {
                    mPointF = controlPoint;
                    break;
                }
            }
            if (mPointF == null) {
                for (PointF controlPoint : mExtremityPoints) {
                    if (getCatchPoint(x, y, controlPoint)) {
                        mPointF = controlPoint;
                        break;
                    }
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mPointF = null;
        }
        if (mPointF != null) {
            mPointF.set(x, y);
        }
        invalidate();
        return true;
    }

    boolean getCatchPoint(float x, float y, PointF controlPoint) {
        float hypot = (float) Math.hypot(x - controlPoint.x, y - controlPoint.y);
        if (hypot <= mPointWidth) {
            controlPoint.set(x, y);
            return true;
        }
        return false;
    }
}
