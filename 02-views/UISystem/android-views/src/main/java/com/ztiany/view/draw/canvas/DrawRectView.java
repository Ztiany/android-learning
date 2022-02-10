package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class DrawRectView extends View {

    public DrawRectView(Context context) {
        this(context, null);
    }

    public DrawRectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint mPaint;
    private Rect mRect;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.set(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(mRect,mPaint);

        canvas.translate(getWidth()/2,getHeight()/2);

        mPaint.setColor(Color.RED);
        canvas.drawRect(mRect,mPaint);
    }
}
