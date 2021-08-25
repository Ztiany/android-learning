package com.ztiany.view.draw.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;


/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-26 10:36                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public class ThirdBezierView extends View {


    private Paint mPaint;
    private Path mPath;
    private Paint mPointPaint;


    private PointF mPointF0, mPointF1,mPointF2;
    private PointF mCurrentP;



    private float mPointWidth;
    private float mPointWidthPercent = 0.1F;

    private float mLastX, mLastY;


    public ThirdBezierView(Context context) {
        this(context, null);
    }

    public ThirdBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mPointF0 = new PointF();
        mPointF1 = new PointF();
        mPointF2 = new PointF();
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(UnitConverter.dpToPx(10));
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(2));


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = Math.min(w, h);
        int centerY = h / 2;
        mPointWidth = min * mPointWidthPercent;
        mPointF0.set(min / 2 - min / 4, centerY);
        mPointF1.set(min / 2 + min / 4, centerY);

        mPointF2.set(min / 2 + min / 4, centerY/2);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPoint(mPointF0.x, mPointF0.y, mPointPaint);
        canvas.drawPoint(mPointF1.x, mPointF1.y, mPointPaint);


        mPath.reset();
        mPath.moveTo(mPointF0.x, mPointF0.y);
        mPath.cubicTo(mLastX, mLastY, mPointF2.x, mPointF2.y,mPointF1.x, mPointF1.y);
        mPath.lineTo(mPointF2.x, mPointF2.y);
        mPath.lineTo(mLastX, mLastY);
        mPath.lineTo(mPointF0.x, mPointF0.y);
        canvas.drawPath(mPath, mPaint);
    }


    PointF getCatchPoint(float x, float y) {
        PointF pf = null;
        if (Math.hypot(x - mPointF1.x, y - mPointF1.y) <= mPointWidth) {
            pf = mPointF1;
        }
        if (Math.hypot(x - mPointF0.x, y - mPointF0.y) <= mPointWidth) {
            pf = mPointF0;
        }
        if (Math.hypot(x - mPointF2.x, y - mPointF2.y) <= mPointWidth) {
            pf = mPointF2;
        }

        return pf;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mCurrentP = getCatchPoint(x, y);
                if (mCurrentP == null) {
                    mLastX = x;
                    mLastY = y;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mCurrentP != null) {
                    mCurrentP.set(x, y);
                } else {
                    mLastX = x;
                    mLastY = y;
                }
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mCurrentP = null;
                break;
            }

        }

        return true;
    }
}
