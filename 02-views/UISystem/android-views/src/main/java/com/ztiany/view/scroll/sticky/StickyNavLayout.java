package com.ztiany.view.scroll.sticky;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.ztiany.view.R;

import java.util.Arrays;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;


/**
 * https://github.com/hongyangAndroid/Android-StickyNavLayout
 */
public class StickyNavLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "StickyNavLayout";

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll() called with: target = [" + target + "], dxConsumed = [" + dxConsumed + "], dyConsumed = [" + dyConsumed + "], dxUnconsumed = [" + dxUnconsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.d(TAG, "onNestedPreScroll() called with: target = [" + target + "], dx = [" + dx + "], dy = [" + dy + "], consumed = [" + Arrays.toString(consumed) + "]");
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight; // 向上滑动并且topView没有完成隐藏
        //向下滑动，自己还可以继续向下滑动并且target不能往下滑动
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "onNestedFling() called with: target = [" + target + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "], consumed = [" + consumed + "]");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling() called with: target = [" + target + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
        //down - //up+
        if (getScrollY() >= mTopViewHeight)
            return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.d(TAG, "getNestedScrollAxes() called");
        return 0;
    }

    private View mTop;//头部View
    private View mNav;//Indicator
    private ViewPager mViewPager;

    private int mTopViewHeight;

    private OverScroller mScroller;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.nested_scroll_rl_top);
        mNav = findViewById(R.id.nested_scroll_indicator);
        View view = findViewById(R.id.nested_scroll_viewpager);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException("id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量头部
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        //ViewPager的高度 = 整个容器的高度 - indicator的高度(当头部被隐藏后)
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        mViewPager.setLayoutParams(params);
        Log.d(TAG, "params.height:" + params.height);
        Log.d(TAG, "getMeasuredHeight():" + getMeasuredHeight());
        Log.d(TAG, "mNav.getMeasuredHeight():" + mNav.getMeasuredHeight());
        Log.d(TAG, "mTop.getMeasuredHeight():" + mTop.getMeasuredHeight());
        Log.d(TAG, "mViewPager.getMeasuredHeight():" + mViewPager.getMeasuredHeight());
        int measuredHeight = mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight();
        Log.d(TAG, "measuredHeight:" + measuredHeight);
        setMeasuredDimension(getMeasuredWidth(), measuredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

}
