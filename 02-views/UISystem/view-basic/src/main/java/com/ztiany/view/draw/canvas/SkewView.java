package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class SkewView extends View {

    private Paint mPaint;
    private Rect rect = new Rect(0, 0, 400, 400);

    public SkewView(Context context) {
        this(context, null);
    }

    public SkewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GREEN);
        canvas.drawRect(rect, mPaint);

        canvas.translate(0, 400);
        // x 方向上向右边倾斜45 度
        canvas.skew(1F, 0);
        mPaint.setColor(0x88FF0000);
        canvas.drawRect(rect, mPaint);

    }
}
