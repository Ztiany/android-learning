package com.ztiany.view.draw.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.ztiany.view.R;
import com.ztiany.view.utils.UnitConverter;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-21 23:37
 */
public class PolyToPolyDemoView extends View {

    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private float[] src = new float[8];//四个原始点
    private float[] dst = new float[8];//四个目标点
    private float left, right, top, bottom;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PolyToPolyDemoView(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_scenery_01);
        mPaint.setStrokeWidth(UnitConverter.dpToPx(5));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float centerX = w / 2;
        float centerY = h / 2;
        left = centerX - mBitmap.getWidth() / 2;
        right = centerX + mBitmap.getWidth() / 2;
        top = centerY - mBitmap.getHeight() / 2;
        bottom = centerY + mBitmap.getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.reset();
        src[0] = left;
        src[1] = top;
        src[2] = right;
        src[3] = top;
        src[4] = left;
        src[5] = bottom;
        src[6] = right;
        src[7] = bottom;

        dst[0] = left - 10;//左上角
        dst[1] = top + 30;
        dst[2] = right + 10;//右上角
        dst[3] = top - 50;
        dst[4] = left + 20;//左下角
        dst[5] = bottom + 20;
        dst[6] = right + 10;//右下角
        dst[7] = bottom + 30;

        mMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        canvas.save();
        canvas.concat(mMatrix);
        canvas.drawBitmap(mBitmap, left, top, null);
        canvas.restore();

        mPaint.setColor(Color.RED);
        canvas.drawPoints(src, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawPoints(dst, mPaint);
    }
}
