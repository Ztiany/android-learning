package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;



public class DrawPointView extends View {

    private RectF mOval;

    public DrawPointView(Context context) {
        this(context, null);
    }

    public DrawPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint;
    private Path mPath;

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(20));
        mOval = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(20));

        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPoint(getWidth() / 3, getHeight() / 2, mPaint);

        mPaint.setColor(Color.BLUE);

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(getWidth() / 4, getHeight() / 2, mPaint);

        mPaint.setColor(Color.YELLOW);

        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(getWidth() / 5, getHeight() / 2, mPaint);

        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(12));

        mPath.moveTo(getWidth() / 2, getHeight() / 2);
        mOval.set(getWidth() / 1.5F, getHeight() / 1.5F, (getWidth() / 1.2F), getHeight() / 1.2F);
        mPath.addArc(mOval, 0, 180);

        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }
}
