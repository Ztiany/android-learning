package com.ztiany.view.custom.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Surface的使用
 */
public class LoadingView extends SurfaceView implements Callback, Runnable {

    private SurfaceHolder holder;
    private boolean isRunning;
    private Canvas mCanvas;
    private int totalSize;
    private int minSize;
    private int leftColor;
    private int rightColor;
    private int leftRadius;
    private int rightRadius;
    private Paint leftPaint;
    private Paint rightPaint;
    private int mSpeed;
    private int[] range;
    private int mode = 1;

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void init() {
        holder = getHolder();
        holder.addCallback(this);
        leftColor = Color.rgb(32, 43, 155);
        rightColor = Color.rgb(231, 89, 12);
        totalSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                30, getResources().getDisplayMetrics());
        minSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics());

        leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        leftPaint.setColor(leftColor);
        rightPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        rightPaint.setColor(rightColor);

        range = new int[2];
        range[0] = totalSize / 2 - minSize;//
        range[1] = minSize - totalSize / 2;//
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            doDraw();
        }
    }

    private void doDraw() {
        try {
            mCanvas = holder.lockCanvas();
            if (mCanvas != null) {
                draw();
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void draw() {
        mCanvas.drawColor(Color.WHITE);
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        leftRadius = totalSize / 2 + getSpeed();
        rightRadius = totalSize - leftRadius;
        int leftCenter = centerX - leftRadius;
        int rightCenter = centerX + rightRadius;

        mCanvas.drawCircle(leftCenter, centerY, leftRadius, leftPaint);
        mCanvas.drawCircle(rightCenter, centerY, rightRadius, rightPaint);

    }

    private int getSpeed() {
        if (mode == 1) {//
            if (mSpeed == range[0]) {
                mode = 2;
            }
            mSpeed++;
        } else if (mode == 2) {//W
            if (mSpeed == range[1]) {
                mode = 1;
            }
            mSpeed--;
        }
        return mSpeed;
    }
}
