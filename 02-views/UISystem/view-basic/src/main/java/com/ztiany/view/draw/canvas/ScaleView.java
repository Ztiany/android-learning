package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;


public class ScaleView extends View {

    private Paint mPaint;

    private RectF mRect;
    private int mWidth;
    private int mHeight;


    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        mPaint.setStrokeWidth(UnitConverter.dpToPx(2));
        mRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRect.set(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (float scale = 1F; scale > 0; scale -= 0.05F) {
            canvas.save();
            canvas.translate(  ( (1 - scale) * mWidth)/2    ,   ((1 - scale) * mHeight)/2 );
            canvas.scale(scale, scale);
            canvas.drawRect(mRect, mPaint);
            canvas.restore();
        }
    }
}
