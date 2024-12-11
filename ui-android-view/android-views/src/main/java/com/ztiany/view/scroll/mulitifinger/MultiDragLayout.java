package com.ztiany.view.scroll.mulitifinger;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.core.view.MotionEventCompat;

/**
 * 处理好多个手指拖动控件
 */
public class MultiDragLayout extends FrameLayout {

    private static final String TAG = MultiDragLayout.class.getSimpleName();
    private float mLastX;
    private float mLastY;
    private float mDownX;//test
    private float mDownY;

    public static final int INVALID_POINTER = MotionEvent.INVALID_POINTER_ID;
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    private int mScaledTouchSlop;
    private boolean mIntercept;

    public MultiDragLayout(Context context) {
        this(context, null);
    }

    public MultiDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "MultiDragLayout() called with: " + "context = [" + context + "], attrs = [" + attrs + "], defStyleAttr = [" + defStyleAttr + "]");
        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.d(TAG, "mScaledTouchSlop:" + mScaledTouchSlop);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                //重置拦截标识
                mIntercept = false;
                //获取初始的位置
                mLastX = ev.getX();
                mLastY = ev.getY();
                mDownX = mLastX;
                mDownY = mLastY;
                //这里我们根据最初的触摸的确定一个pointerId
                int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);

                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {

                Log.d(TAG, "onInterceptTouchEvent() called with: " + "ev = [ACTION_POINTER_DOWN  ]");

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //如果我们关系的pointerId==-1，不再拦截
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                float currentY = MotionEventCompat.getY(ev, pointerIndex);
                float currentX = MotionEventCompat.getX(ev, pointerIndex);

                if (!mIntercept) {
                    float offset = Math.abs(mDownY - currentY);
                    Log.d(TAG, "offset:" + offset);
                    if (offset >= mScaledTouchSlop) {
                        float dx = mLastX - currentX;
                        float dy = mLastY - currentY;
                        if (Math.abs(dy) > Math.abs(dx)) {
                            mIntercept = true;
                        }

                    }
                }
                mLastX = currentX;
                mLastY = currentY;
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                Log.d(TAG, "onInterceptTouchEvent() called with: " + "ev = [ACTION_POINTER_UP  ]");
                //处理手指抬起
                onSecondaryPointerUp(ev);
                break;
            }
            case MotionEvent.ACTION_UP: {
                mIntercept = false;
                mActivePointerId = INVALID_POINTER;
                break;
            }
        }
        return mIntercept;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int index = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, index);
        if (mActivePointerId == pointerId) {//如果是主要的手指抬起
            final int newPointerIndex = index == 0 ? 1 : 0;//确认一个还在屏幕上手指的index
            mLastY = MotionEventCompat.getY(ev, newPointerIndex);//更新lastY的值
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);//更新pointerId
        }
    }

    private void onSecondaryPointerDown(MotionEvent ev) {
        final int index = MotionEventCompat.getActionIndex(ev);
        mLastY = MotionEventCompat.getY(ev, index);
        mActivePointerId = MotionEventCompat.getPointerId(ev, index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = ev.getX();
                mLastY = ev.getY();
                mDownX = mLastX;
                mDownY = mLastY;
                int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.d(TAG, "onTouchEvent() called with: " + "ev = [ACTION_POINTER_DOWN  ]");
                onSecondaryPointerDown(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                printSamples(ev);
                int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);//主手指的索引
                if (pointerIndex < 0) {
                    return false;
                }
                float currentX = MotionEventCompat.getX(ev, pointerIndex);
                float currentY = MotionEventCompat.getY(ev, pointerIndex);
                int dy = (int) (mLastY - currentY);
                scrollBy(0, dy);
                mLastX = currentX;
                mLastY = currentY;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                Log.d(TAG, "onTouchEvent() called with: " + "ev = [ACTION_POINTER_UP  ]");
                onSecondaryPointerUp(ev);
                break;
            }
            case MotionEvent.ACTION_UP: {
                mIntercept = false;
                mActivePointerId = INVALID_POINTER;
                break;
            }
        }
        return true;
    }

    //for test
    void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        Log.d(TAG + "his", "historySize:" + historySize);
        Log.d(TAG + "his", "pointerCount:" + pointerCount);
        for (int h = 0; h < historySize; h++) {
            Log.d(TAG + "his", "ev.getHistoricalEventTime(h):" + ev.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                Log.d(TAG + "his", "ev.getPointerId(p):" + ev.getPointerId(p));
                Log.d(TAG + "his", "ev.getPointerId(p):" + ev.getHistoricalX(p, h));
                Log.d(TAG + "his", "ev.getPointerId(p):" + ev.getHistoricalY(p, h));
            }
        }
        Log.d(TAG, "ev.getEventTime():" + ev.getEventTime());
        for (int p = 0; p < pointerCount; p++) {
            Log.d(TAG + "his", "ev.getPointerId(p):" + ev.getPointerId(p));
            Log.d(TAG + "his", "ev.getX(p):   and    ev.getY(p):" + ev.getX(p) + "   " + ev.getY(p));
        }
    }

}