package com.ztiany.view.scroll.nestscroll.sampl2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

public class NestParentLayout extends FrameLayout implements NestedScrollingParent {

    private static final String TAG = NestParentLayout.class.getSimpleName();

    private NestedScrollingParentHelper mScrollingParentHelper;

    public NestParentLayout(Context context) {
        this(context, null);
    }

    public NestParentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    /*
    子类开始请求滑动
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll() called with: " + "child = [" + child + "], target = [" + target + "], nestedScrollAxes = [" + nestedScrollAxes + "]");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        mScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public int getNestedScrollAxes() {
        return mScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(@NonNull View child) {
        mScrollingParentHelper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d(TAG, "onNestedPreScroll() called with: " + "dx = [" + dx + "], dy = [" + dy + "], consumed = [" + Arrays.toString(consumed) + "]");
        if (dx > 0) {
            if (target.getRight() + dx > getWidth()) {
                dx = target.getRight() + dx - getWidth();//多出来的
                offsetLeftAndRight(dx);
                consumed[0] += dx;//父亲消耗
            }
        } else {
            if (target.getLeft() + dx < 0) {
                dx = dx + target.getLeft();
                offsetLeftAndRight(dx);
                Log.d(TAG, "dx:" + dx);
                consumed[0] += dx;//父亲消耗
            }
        }

        if (dy > 0) {
            if (target.getBottom() + dy > getHeight()) {
                dy = target.getBottom() + dy - getHeight();
                offsetTopAndBottom(dy);
                consumed[1] += dy;
            }
        } else {
            if (target.getTop() + dy < 0) {
                dy = dy + target.getTop();
                offsetTopAndBottom(dy);
                Log.d(TAG, "dy:" + dy);
                consumed[1] += dy;//父亲消耗
            }
        }
    }

}