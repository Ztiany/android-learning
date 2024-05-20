package com.ztiany.view.draw.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 计算Matrix
 */
public class CanvasSetMatrixDemoView extends View {

    private static final String TAG = CanvasSetMatrixDemoView.class.getSimpleName();

    private Paint mPaint;
    private Rect mRect;
    private Matrix mMatrix;

    public CanvasSetMatrixDemoView(Context context) {
       this(context , null);
    }

    public CanvasSetMatrixDemoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CanvasSetMatrixDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMatrix = new Matrix();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.reset();
        //canvas应用了新的Matrix，从而View树中Matrix的累计变换不会影响到此处的绘制，canvas的绘制从根view的定点开始
        mRect.set(0, 0, 400, 400);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);

        mMatrix.setTranslate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.setMatrix(mMatrix);
        mPaint.setColor(Color.BLACK);
        mRect.set(0, 0, 400, 400);
        canvas.drawRect(mRect, mPaint);

        mMatrix.preRotate(20);
        canvas.setMatrix(mMatrix);
        mPaint.setColor(Color.GRAY);
        mRect.set(0, 0, 400, 400);
        canvas.drawRect(mRect, mPaint);
        mMatrix.preTranslate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);

        canvas.setMatrix(mMatrix);
        mRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(0x550000FF);
        canvas.drawRect(mRect, mPaint);

        mPaint.setColor(Color.RED);
        mRect.set(0, 0, 400, 400);
        canvas.drawRect(mRect, mPaint);
        Log.d(TAG, "mMatrix:" + mMatrix);
    }
}
