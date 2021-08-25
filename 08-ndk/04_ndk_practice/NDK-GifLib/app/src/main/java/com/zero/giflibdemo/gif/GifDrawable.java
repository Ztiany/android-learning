package com.zero.giflibdemo.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GifDrawable extends Drawable implements Animatable, Runnable {

    private Paint mPaint;

    /*
     1. 首先 Java 准备一个Bitmap 传给 C++
     2. gif 解压出来之后每一帧(Screen) 填充成Bitmap
     3. Bitmap 绘制到 Canvas
     4. Bitmap 是要 Java 端初始化出来的
     */
    private Bitmap mBitmap;

    private Rect srcRect;//gif的矩形区域

    private GifFrame mGifFrame;

    //gif在C++解码  width
    private int width;

    //gif在C++解码  height
    private int height;

    private boolean isRunning;

    long showTime;

    //当前帧要显示的时间 C++ 获取的
    long curTime;

    int frameCount;

    int frameIndex;

    public GifDrawable(GifFrame gifFrame) {
        mGifFrame = gifFrame;

        width = mGifFrame.getWidth();
        height = mGifFrame.getHeight();
        frameCount = mGifFrame.getFrameCount();

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);

        frameIndex = 0;
        srcRect = new Rect(0, 0, width, height);
        mGifFrame.getFrame(mBitmap, getFrameIndex());
    }

    public int getFrameIndex() {
        frameIndex++;
        return frameIndex < frameCount ? frameIndex : (frameIndex = 0);
    }

    @Override
    public void draw(@NonNull final Canvas canvas) {
        //绘制 把gif的每一帧绘制到canvas
        canvas.drawBitmap(mBitmap, srcRect, getBounds(), mPaint);
        showTime = SystemClock.uptimeMillis();
        if (isRunning()) {
            scheduleSelf(this, showTime + curTime);
        }
    }

    @Override
    public void setAlpha(final int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            isRunning = true;
            scheduleSelf(this, SystemClock.uptimeMillis());
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            isRunning = false;
            unscheduleSelf(this);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        //获取每一帧
        curTime = mGifFrame.getFrame(mBitmap, getFrameIndex());
        invalidateSelf();
    }

}
