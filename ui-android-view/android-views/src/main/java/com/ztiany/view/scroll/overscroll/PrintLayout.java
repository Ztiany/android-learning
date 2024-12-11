package com.ztiany.view.scroll.overscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class PrintLayout extends FrameLayout {

    private static final String TAG = PrintLayout.class.getSimpleName();

    public PrintLayout(Context context) {
        this(context, null);
    }

    public PrintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent() called with: ev = [" + ev + "]");
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw() called with: " + "canvas = [" + canvas + "]");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        Log.d(TAG, "invalidate() called with: " + "");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw() called with: " + "canvas = [" + canvas + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG + " ", "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG + " ", "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG + " ", "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
    }

}
