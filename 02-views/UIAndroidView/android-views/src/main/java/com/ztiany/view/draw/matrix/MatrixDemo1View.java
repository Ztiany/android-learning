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
 * 理解Matrix的变换顺序
 */
public class MatrixDemo1View extends View {

    private static final String TAG = MatrixDemo1View.class.getSimpleName();

    private Paint mPaint;
    private Rect mRect;
    private Matrix mMatrix;
    int size = 200;

    public MatrixDemo1View(Context context) {
        this(context, null);
    }

    public MatrixDemo1View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixDemo1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMatrix = new Matrix();
        mRect = new Rect();
    }

    //理解循序(相对于原坐标)：
    //postTranslate(a, b); -->   mMatrix.postRotate(20); -->   mMatrix.preTranslate(-a, -b); =
    //平移到中心——>旋转20°
    //理解循序(相对于变换后坐标)：
    //旋转20°——>平移到中心
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mMatrix.reset();

        canvas.save();
        mRect.set(0, 0, size, size);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);
        canvas.restore();

        canvas.save();
        mMatrix.postTranslate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.concat(mMatrix);
        mPaint.setColor(Color.BLACK);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);
        canvas.restore();

        canvas.save();
        mMatrix.postRotate(20);
        canvas.concat(mMatrix);
        mPaint.setColor(Color.GRAY);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);
        canvas.restore();

        canvas.save();
        mMatrix.preTranslate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);
        canvas.concat(mMatrix);
        mRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(0x550000FF);
        canvas.drawRect(mRect, mPaint);

        mPaint.setColor(Color.RED);
        mRect.set(0, 0, size, size);
        canvas.drawRect(mRect, mPaint);
        canvas.restore();


        Log.d(TAG, "mMatrix:" + mMatrix);
    }
}
