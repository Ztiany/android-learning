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

import java.util.ArrayList;
import java.util.List;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-26 10:36                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public class BezierView extends View {


    private Paint mPaint;
    private Path mPath;
    private Paint mPointPaint;


    private PointF mPointF0, mPointF1;
    private PointF mCurrentP;


    private List<PointF> mPointList;


    private float mPointWidth;
    private float mPointWidthPercent = 0.1F;

    private float mLastX, mLastY;


    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mPointF0 = new PointF();
        mPointF1 = new PointF();
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(UnitConverter.dpToPx(10));
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(2));

        mPointList = new ArrayList<>();

        for (float i = 0F; i < 1F; i += 0.01F) {
            mPointList.add(new PointF());
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = Math.min(w, h);
        int centerY = h / 2;
        mPointWidth = min * mPointWidthPercent;
        mPointF0.set(min / 2 - min / 4, centerY);
        mPointF1.set(min / 2 + min / 4, centerY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPoint(mPointF0.x, mPointF0.y, mPointPaint);
        canvas.drawPoint(mPointF1.x, mPointF1.y, mPointPaint);


       setPoints();
        for (PointF pointF : mPointList) {
            canvas.drawPoint(pointF.x, pointF.y, mPaint);
        }



   /*     mPath.reset();
        mPath.moveTo(mPointF0.x,mPointF0.y);
        mPath.quadTo( mLastX, mLastY,mPointF1.x, mPointF1.y);
        canvas.drawPath(mPath, mPaint);*/
    }

    private void setPoints() {
        float temp = 0F;
        float add = 0.01F;
        float x = 0;
        float y = 0;
        for (PointF pointF : mPointList) {
            x = (1 - temp) * (1 - temp) * mPointF0.x + 2 * temp * (1 - temp) * mLastX + (temp * temp) * mPointF1.x;
            y = (1 - temp) * (1 - temp) * mPointF0.y + 2 * temp * (1 - temp) * mLastY + (temp * temp) * mPointF1.y;
            pointF.set(x, y);
            temp += add;
        }
    }


    PointF getCatchPoint(float x, float y) {
        PointF pf = null;
        if (Math.hypot(x - mPointF1.x, y - mPointF1.y) <= mPointWidth) {
            pf = mPointF1;
        }
        if (Math.hypot(x - mPointF0.x, y - mPointF0.y) <= mPointWidth) {
            pf = mPointF0;
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
                mLastX = x;
                mLastY = y;
                mCurrentP = getCatchPoint(x, y);
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
