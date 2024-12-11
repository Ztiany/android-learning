package com.ztiany.view.custom.custom_viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * 练习滑动冲突的解决方式(外部拦截法)
 */
public class HScrollLayout extends ViewGroup {

    public static final String TAG = HScrollLayout.class.getSimpleName();

    public HScrollLayout(Context context) {
        this(context, null);
    }

    public HScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mLastEventX, mLastEventY;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mWidth;
    private int mCurrentPage;

    private float minVelocity;
    private float maxVelocity;

    private void init() {
        //设置方向为横向布局
        mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        mVelocityTracker = VelocityTracker.obtain();

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getChildCount() < 0) {
            return false;
        }
        boolean intercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercept = true;
                } else {
                    //如果希望子view能接收到事件，DOWN必然要返回false
                    intercept = false;
                    mLastEventX = x;
                    mLastEventX = y;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                //计算移动差
                int dx = x - mLastEventX;
                int dy = y - mLastEventY;
                if (Math.abs(dx) > Math.abs(dy)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起，必须返回false，因为返回值对自己没有影响，而对子view可能有影响
                intercept = false;
                break;
        }
        mLastEventX = x;
        mLastEventY = y;
        return intercept;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "l:" + l);
        Log.d(TAG, "t:" + t);
        Log.d(TAG, "r:" + r);
        Log.d(TAG, "b:" + b);

        int left = l, top = t, right = r, bottom = b;
        if (changed) {
            int childCount = getChildCount();
            View child;
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                child.layout(left, top, left + child.getMeasuredWidth(), bottom);
                left += child.getMeasuredWidth();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        int x = (int) event.getX();
        int y = (int) event.getY();

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastEventX = x;
                mLastEventY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastEventX;
                scrollBy(-dx, 0);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //将要滑动的距离
                int distanceX;
                mVelocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float xVelocity = mVelocityTracker.getXVelocity();
                Log.d(TAG, "xVelocity:" + xVelocity);
                if (Math.abs(xVelocity) > minVelocity) {
                    if (xVelocity > 0) {//向左
                        mCurrentPage--;
                    } else {
                        mCurrentPage++;
                    }
                } else {
                    // 不考虑加速度
                    Log.d(TAG, "getScrollX():" + getScrollX());
                    if (getScrollX() < 0) {//说明超出左边界
                        mCurrentPage = 0;
                    } else {
                        int childCount = getChildCount();
                        int maxScroll = (childCount - 1) * mWidth;
                        Log.d(TAG, "maxScroll:" + maxScroll);
                        if (getScrollX() > maxScroll) {//超出了右边界
                            mCurrentPage = getChildCount() - 1;
                        } else {
                            //在边界范围内滑动
                            int currentScrollX = mCurrentPage * mWidth;//已近产生的偏移
                            int offset = getScrollX() % mWidth;
                            Log.d(TAG, "mWidth:" + mWidth);
                            Log.d(TAG, "offset:" + offset);

                            if (currentScrollX > Math.abs(getScrollX())) {//向左偏移
                                if (offset < (mWidth - mWidth / 3)) {//小于其 2/3
                                    mCurrentPage--;
                                } else {

                                }
                            } else {//向右偏移

                                if (offset > mWidth / 3) {//小于其 2/3
                                    mCurrentPage++;
                                } else {

                                }
                            }

                        }
                    }
                    //不考虑加速度
                }
                Log.d(TAG, "mCurrentPage:" + mCurrentPage);
                mCurrentPage = (mCurrentPage < 0) ? 0 : (Math.min(mCurrentPage, (getChildCount() - 1)));
                distanceX = mCurrentPage * mWidth - getScrollX();
                Log.d(TAG, "getScrollX():" + getScrollX());
                Log.d(TAG, "distanceX:" + distanceX);
                smoothScroll(distanceX, 0);
                mVelocityTracker.clear();
                break;
        }
        mLastEventX = x;
        mLastEventY = y;
        //返回true，处理事件
        return true;
    }

    private void smoothScroll(int distanceX, int distanceY) {
        mScroller.startScroll(getScrollX(), 0, distanceX, 0);
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }

    /**
     * 重写测量逻辑
     *
     * @param heightMeasureSpec 这里我们不考虑wrap_content的情况，也不考虑子view的margin情况
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        View child;

        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

}