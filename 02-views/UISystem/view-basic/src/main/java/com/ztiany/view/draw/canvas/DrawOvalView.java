package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class DrawOvalView extends View {

    public DrawOvalView(Context context) {
        this(context, null);
    }

    public DrawOvalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawOvalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint;
    private RectF mRectF;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(getWidth() / 2, getHeight() / 2, getWidth() / 1.2F, getHeight() / 1.2F);
        mPaint.setColor(Color.RED);

        canvas.drawOval(mRectF,mPaint);


    }
}
