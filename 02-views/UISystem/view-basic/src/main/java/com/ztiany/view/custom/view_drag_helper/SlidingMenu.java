package com.ztiany.view.custom.view_drag_helper;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import  androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlidingMenu extends ViewGroup {

    private ViewDragHelper viewDragHelper;
    private View menuView;
    private View contentView;
    private static final float FLOAT = 0.2f;//默认侧滑菜单比内容控件少的百分比
    private boolean isMenuOpened;

    @SuppressLint("NewApi")
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context) {
        this(context, null);
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, new SlidingCallBack());
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);//设计可以边缘滑动
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(sizeWidth, sizeHeight);
        // 内容
        contentView.measure(
                MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.EXACTLY),//设置内容控件的大小和容器一样
                MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.EXACTLY));
        // 菜单
        LayoutParams meLayoutParams = menuView.getLayoutParams();
        menuView.measure(
                getChildMeasureSpec(widthMeasureSpec, (int) (sizeWidth * FLOAT), meLayoutParams.width),
                getChildMeasureSpec(heightMeasureSpec, 0, meLayoutParams.height));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            contentView.layout(l, t, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
            menuView.layout(-menuView.getMeasuredWidth(), t, 0, menuView.getMeasuredHeight());
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {//事件交给ViewDragHelper处理
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {//事件交给VIewDragHelper处理
        viewDragHelper.processTouchEvent(event);
        return true;
    }


    private class SlidingCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View arg0, int arg1) {
            if (arg0 == menuView) {//如果是菜单
                return isMenuOpened;//菜单打开了
            } else {
                return false;
            }
        }

        //当发生边界事件
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //此时直接调用captureChildView方法，捕获menuView进入拖动状态
            viewDragHelper.captureChildView(menuView, pointerId);
        }

        /**
         * child 当前拖动的View
         * left 建议拖动到的距离
         * dx 偏移量
         * left + dx = child.getLeft();
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {//返回应当到达的位置
            return Math.max(-child.getWidth(), Math.min(left, 0));
        }

        //返回拖动view的触摸位置范围，返回1也可以。
        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if (child == menuView) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            } else {
                return 0;
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {

        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {

        }

        // 当拖动的view 被释放
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {//释放
            if (releasedChild == menuView) {
                int absLeft = Math.abs(menuView.getLeft());
                if (absLeft >= menuView.getMeasuredWidth() / 2) {// 往左
                    performMenu(false);
                } else {// 往右
                    performMenu(true);
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("must has two child");
        }
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    private void performMenu(boolean isOpen) {
        int targetX;
        if (!isOpen) {
            targetX = -menuView.getMeasuredWidth();
            isMenuOpened = false;
        } else {
            targetX = 0;
            isMenuOpened = true;
        }
        viewDragHelper.settleCapturedViewAt(targetX, 0);
        invalidate();
    }

}