package com.xiangxue.common.fragment;

/**
 * Created by webull on 17/5/3.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by tonghongliang on 16/5/8.
 * To fix a issue which action bar does not collapse in layout the CoordinatorLayout -> AppBarLayout -> Toobar -> ViewPager -> RecyclerView -> Cardview -> RecyclerView
 */
public class NestedLogRecyclerView extends RecyclerView {
    public NestedLogRecyclerView(Context context) {
        super(context);
        init();
    }

    public NestedLogRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedLogRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
    }

    private static final String TAG = "RecyclerViewNestedLog";
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.e("RecyclerViewNestedLog", "setNestedScrollingEnabled");
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.e("RecyclerViewNestedLog", "isNestedScrollingEnabled");
        return super.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.e("RecyclerViewNestedLog", "startNestedScroll");
        return super.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        Log.e("RecyclerViewNestedLog", "startNestedScroll");
        return super.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll() {
        Log.e("RecyclerViewNestedLog", "stopNestedScroll");
        super.stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        Log.e("RecyclerViewNestedLog", "stopNestedScroll");
        super.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.e("RecyclerViewNestedLog", "hasNestedScrollingParent");
        return super.hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        Log.e("RecyclerViewNestedLog", "hasNestedScrollingParent");
        return super.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedScroll");
        return super.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedScroll");
        return super.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedPreScroll");
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedPreScroll");
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow,
                type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedFling");
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.e("RecyclerViewNestedLog", "dispatchNestedPreFling");
        return super.dispatchNestedPreFling(velocityX, velocityY);
    }
}

