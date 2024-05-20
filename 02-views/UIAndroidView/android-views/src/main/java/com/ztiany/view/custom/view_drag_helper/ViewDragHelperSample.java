package com.ztiany.view.custom.view_drag_helper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import  androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-13 22:55
 */
public class ViewDragHelperSample extends FrameLayout {

    private static final String TAG = ViewDragHelperSample.class.getSimpleName();

    private ViewDragHelper mViewDragHelper;


    public ViewDragHelperSample(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1F, new SampleCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    private class SampleCallback extends ViewDragHelper.Callback {

        /**
         * 返回true，表示任何子view都可以被捕获
         *
         * @param child     将要被捕获的子view
         * @param pointerId eventId
         * @return 返回true后，子view就表示子view被捕获了
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            Log.d(TAG, "onViewDragStateChanged() state = [" + state + "]");
            //ViewDragHelper有三种状态，如下
            //ViewDragHelper.STATE_IDLE：0 空闲
            // ViewDragHelper.STATE_DRAGGING ：1 正在拖动
            //ViewDragHelper.STATE_SETTLING：2 释放后某个View正在返回原理的位置
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }


        @Override
        public boolean onEdgeLock(int edgeFlags) {
            //true的时候会锁住当前的边界，false则unLock。
            return super.onEdgeLock(edgeFlags);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override
        public int getOrderedChildIndex(int index) {
            //确定子视图的Z轴顺序。
            return super.getOrderedChildIndex(index);
        }

        /*
        如果子view会消耗事件，那么下面方法必须实现，原因是
            如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入这里的onTouchEvent，在onTouchEvent的DOWN的时候就确定了captureView。
            如果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法：getViewHorizontalDragRange和getViewVerticalDragRange，
            只有这两个方法返回大于0的值才能正常的捕获。
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        //下面两个方法用于控制被拖动子view的边界
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    }
}
