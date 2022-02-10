package com.ztiany.view.draw.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztiany.view.R;

import androidx.annotation.Nullable;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 00:05
 */
public class CameraDemoView extends View {

    private Bitmap mBitmap;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();

    private int mRotateX;
    private int mRotateY;
    private int mRotateZ;
    private int mTranslateZ = -8;

    private int mPox;
    private int mPoy;

    private boolean mIsMatrixToCanvas = false;

    public CameraDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_scenery_02);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.save();
        mCamera.save();
        mCamera.setLocation(0, 0, mTranslateZ);
        mCamera.rotate(mRotateX, mRotateY, mRotateZ);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        mMatrix.postTranslate(mPox, mPoy);
        mMatrix.preTranslate(-mPox, -mPoy);
        canvas.concat(mMatrix);
        if (mIsMatrixToCanvas) {
            int centerX = getMeasuredWidth() / 2;
            int centerY = getMeasuredHeight() / 2;
            canvas.drawBitmap(mBitmap, centerX - mBitmap.getWidth() / 2, centerY - mBitmap.getHeight() / 2, null);
        } else {
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
        canvas.restore();
    }

    public void setRotateX(boolean add) {
        if (add) {
            mRotateX += 5;
        } else {
            mRotateX -= 5;
        }
        postInvalidate();
    }

    public void setRotateY(boolean add) {
        if (add) {
            mRotateY += 5;
        } else {
            mRotateY -= 5;
        }
        postInvalidate();
    }

    public void setRotateZ(boolean add) {
        if (add) {
            mRotateZ += 5;
        } else {
            mRotateZ -= 5;
        }
        postInvalidate();
    }

    public void setTranslateZ(boolean add) {
        if (add) {
            mTranslateZ += 5;
        } else {
            mTranslateZ -= 5;
        }
        postInvalidate();
    }

    public int getRotateX() {
        return mRotateX;
    }

    public int getRotateY() {
        return mRotateY;
    }

    public int getRotateZ() {
        return mRotateZ;
    }

    public int getTranslateZ() {
        return mTranslateZ;
    }

    public void reset() {
        mRotateX = 0;
        mRotateY = 0;
        mRotateZ = 0;
        mTranslateZ = -8;
        mPox = 0;
        mPoy = 0;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mPox = (int) event.getX();
        mPoy = (int) event.getY();
        postInvalidate();
        return true;
    }

    public void useMatrixWithBitmap() {
        mIsMatrixToCanvas = false;
        invalidate();
    }

    public void useMatrixWithCanvas() {
        mIsMatrixToCanvas = true;
        invalidate();
    }
}
