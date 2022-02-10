package com.ztiany.view.draw.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ztiany.view.R;

import androidx.annotation.Nullable;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 12:34
 */
public class Camera3DTheoryView extends View {


    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private float mDegree;
    private boolean drawOne = true;
    private boolean drawTwo = true;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public Camera3DTheoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap1 = scaleBitmap(mBitmap1);
        mBitmap2 = scaleBitmap(mBitmap2);
        Log.d("Camera3DTheoryView", "w:" + w);
        Log.d("Camera3DTheoryView", "h:" + h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        float y = (mDegree / 90) * (getMeasuredHeight());
        Log.d("Camera3DTheoryView", "y:" + y);
        Log.d("Camera3DTheoryView", "mDegree:" + mDegree);

        if (drawOne) {
            mCamera.save();
            mMatrix.reset();
            mCamera.rotateX(-mDegree);
            mCamera.getMatrix(mMatrix);
            mCamera.restore();
            mMatrix.preTranslate(-getMeasuredWidth() / 2, 0);
            mMatrix.postTranslate(getMeasuredWidth() / 2, y);
            canvas.drawBitmap(mBitmap1, mMatrix, null);

        }

        if (drawTwo) {
            mMatrix.reset();
            mCamera.save();
            mCamera.rotateX((90 - mDegree));
            mCamera.getMatrix(mMatrix);
            mCamera.restore();
            mMatrix.preTranslate(-getMeasuredWidth() / 2, -getMeasuredHeight());
            mMatrix.postTranslate(getMeasuredWidth() / 2, y);
            canvas.drawBitmap(mBitmap2, mMatrix, null);
        }

        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(getMeasuredWidth() / 2, y, 10, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(getMeasuredWidth() / 2, -getMeasuredHeight() + y, 10, mPaint);
        canvas.restore();
    }

    public void onProgressChanged(int progress) {
        mDegree = progress;
        invalidate();
    }

    public void showOne() {
        drawTwo = false;
        drawOne = true;
        invalidate();
    }

    public void showTwo() {
        drawOne = false;
        drawTwo = true;
        invalidate();
    }

    public void showAll() {
        drawTwo = true;
        drawOne = true;
        invalidate();
    }

    private Bitmap scaleBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) getMeasuredWidth()) / width;
        float scaleHeight = ((float) getMeasuredHeight()) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
    }
}
