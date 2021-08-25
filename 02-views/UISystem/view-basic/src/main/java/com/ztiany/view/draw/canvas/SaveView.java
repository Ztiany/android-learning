package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class SaveView extends View{


    private Paint mPaint;
    private RectF mRect;

    private static final String TAG = SaveView.class.getSimpleName();

    public SaveView(Context context) {
        this(context , null);
    }

    public SaveView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public SaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        mRect.set(200, 200, 400, 400);

        canvas.clipRect(mRect);
        canvas.drawColor(Color.RED);
        canvas.drawArc(mRect,0,270,false,mPaint);

        canvas.save();
        mRect.set(200,200,300,300);
        canvas.clipRect(mRect);
        canvas.drawColor(Color.GREEN);
        canvas.save();
        canvas.save();
        canvas.save();

        Log.d(TAG, "canvas.getSaveCount():" + canvas.getSaveCount());

        canvas.restoreToCount(3);
        Log.d(TAG, "canvas.getSaveCount():" + canvas.getSaveCount());

    }
}
