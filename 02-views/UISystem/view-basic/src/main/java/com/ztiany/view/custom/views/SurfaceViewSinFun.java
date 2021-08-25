package com.ztiany.view.custom.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 正弦曲线
 * todo fix overdraw
 */
public class SurfaceViewSinFun extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    //绘图的Canvas 
    private Canvas mCanvas;
    //子线程标志位 
    private volatile boolean mIsDrawing;

    @SuppressWarnings("all")
    private int x = 0, y = 0;

    private Paint mPaint;
    private Path mPath;

    public SurfaceViewSinFun(Context context) {
        this(context, null);
    }

    public SurfaceViewSinFun(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewSinFun(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mPath.moveTo(0, 100);
        initView();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            drawSomething();
            x += 1;
            y = (int) (100 * Math.sin(2 * x * Math.PI / 180));
            mPath.lineTo(x, y);
        }
    }

    private void drawSomething() {
        try {
            //获得canvas对象 
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.translate(0, getMeasuredHeight() / 2);
            //绘制背景 
            mCanvas.drawColor(Color.WHITE);
            //绘制路径 
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                //释放canvas对象并发送消息来绘制 
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }

} 