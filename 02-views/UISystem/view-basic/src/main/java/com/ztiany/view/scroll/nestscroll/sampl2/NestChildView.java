package com.ztiany.view.scroll.nestscroll.sampl2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class NestChildView extends View implements NestedScrollingChild {

    private static final String TAG = NestChildView.class.getSimpleName();

    private float mDownX;//手指第一次落下时的x位置（忽略）
    private float mDownY;//手指第一次落下时的y位置

    private int[] consumed = new int[2];//消耗的距离

    private int[] offsetInWindow = new int[2];//窗口偏移

    private NestedScrollingChildHelper mScrollingChildHelper;

    public NestChildView(Context context) {
        this(context, null);
    }

    public NestChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mDownX = x;
                mDownY = y;
                //当开始滑动的时候，告诉父view
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL | ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int dy = (int) (y - mDownY);
                int dx = (int) (x - mDownX);
                //分发触屏事件给父类处理
                if (dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)) {
                    //减掉父类消耗的距离
                    dx -= consumed[0];
                    dy -= consumed[1];
                    Log.d(TAG, Arrays.toString(offsetInWindow));
                }
                offsetTopAndBottom(dy);
                offsetLeftAndRight(dx);
                break;
            }

            case MotionEvent.ACTION_UP: {
                stopNestedScroll();
                break;
            }
        }
        return true;
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        boolean b = mScrollingChildHelper.startNestedScroll(axes);
        Log.d(TAG, "startNestedScroll() called with: axes = [" + axes + "]" + "  result = " + b);
        return b;
    }

    @Override
    public void stopNestedScroll() {
        mScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * @param dx       水平滑动距离
     * @param dy       垂直滑动距离
     * @param consumed 父类消耗掉的距离
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}