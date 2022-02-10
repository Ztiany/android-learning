package com.ztiany.view.bitmap.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * setImageStream(getContext().getAssets().open("qm.jpg"));
 */
public class BitmapRegionDecoderLargeImageView extends View {

    private static final String TAG = "LargeImageView";

    private int mImageWidth, mImageHeight;
    private Rect mRect;
    private BitmapFactory.Options mOptions;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private float mLastX, mLastY;

    public BitmapRegionDecoderLargeImageView(Context context) {
        this(context, null);
    }

    public BitmapRegionDecoderLargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapRegionDecoderLargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.RED);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int left = (int) (mImageWidth / 2F - measuredWidth / 2F);
        int top = (int) (mImageHeight / 2F - measuredHeight / 2F);
        mRect.set(left, top, left + measuredWidth, top + measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapRegionDecoder != null) {
            canvas.drawBitmap(mBitmapRegionDecoder.decodeRegion(mRect, mOptions), 0, 0, null);
        }
    }

    public void setImageStream(InputStream imageStream) {
        try {
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, mOptions);
            mImageWidth = mOptions.outWidth;
            mImageHeight = mOptions.outHeight;

            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(imageStream, false);
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float dx = mLastX - x;
                float dy = mLastY - y;
                onMove(dx, dy);
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        return true;
    }

    private void onMove(float moveX, float moveY) {
        Log.d(TAG, "onMove() called with: moveX = [" + moveX + "], moveY = [" + moveY + "]");
        Log.d(TAG, "onMove() called with: mImageWidth = [" + mImageWidth + "], mImageHeight = [" + mImageHeight + "]");
        if (mImageWidth > getWidth()) {
            mRect.offset((int) moveX, 0);
            invalidate();
        }
        if (mImageHeight > getHeight()) {
            mRect.offset(0, (int) moveY);
            invalidate();
        }
    }

}