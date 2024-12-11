package com.ztiany.view.scroll.overscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

/**
 * 演示如何实现OverScroll
 */
public class OverScrollerView extends LinearLayout {

    private static final String TAG = OverScrollerView.class.getSimpleName();

    private OverScroller mOverScroller;

    private int mScaledTouchSlop;
    private int mScaledMaximumFlingVelocity;
    private int mScaledMinimumFlingVelocity;
    private int mOverScrollDistance;

    private VelocityTracker mVelocityTracker;

    private boolean mIsBeginDrag;

    private int mActivePointerId;

    private int mLastX, mLastY;

    public OverScrollerView(Context context) {
        this(context, null);
    }

    public OverScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(getContext());
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        mScaledMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
        mOverScrollDistance = viewConfiguration.getScaledOverscrollDistance();//默认为0
        Log.d(TAG, "mOverScrollDistance:" + mOverScrollDistance);
        mOverScrollDistance = 300;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw() called with: " + "canvas = [" + canvas + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
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
    public void invalidate(Rect dirty) {
        Log.d(TAG, "invalidate() called with: " + "dirty = [" + dirty + "]");
        super.invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        Log.d(TAG, "invalidate() called with: " + "l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        int actionMasked = MotionEventCompat.getActionMasked(event);

        if ((actionMasked == MotionEvent.ACTION_MOVE) && (mIsBeginDrag)) {
            return true;
        }

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                if (!mOverScroller.isFinished()) {
                    mIsBeginDrag = true;
                    mOverScroller.abortAnimation();
                } else {
                    mIsBeginDrag = false;
                }
                int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                int downX = (int) MotionEventCompat.getX(event, index);
                int downY = (int) MotionEventCompat.getY(event, index);
                mLastX = downX;
                mLastY = downY;
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                onSecondPointerDown(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return false;
                }
                int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                int currentX = (int) MotionEventCompat.getX(event, pointerIndex);
                int currentY = (int) MotionEventCompat.getY(event, pointerIndex);
                int dx = mLastX - currentX;
                int dy = mLastY - currentY;

                if (Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop) {
                    mIsBeginDrag = true;
                    mLastX = currentX;
                    mLastY = currentY;
                }

                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mIsBeginDrag = false;
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                onSecondPointerUp(event);
                break;
            }
        }

        return mIsBeginDrag;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        mVelocityTracker.addMovement(event);

        switch (actionMasked) {

            case MotionEvent.ACTION_DOWN: {
                if ((mIsBeginDrag = !mOverScroller.isFinished())) {
                    mOverScroller.abortAnimation();
                }
                int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                mLastX = (int) MotionEventCompat.getX(event, index);
                mLastY = (int) MotionEventCompat.getY(event, index);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                onSecondPointerDown(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return false;
                }
                int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                int currentX = (int) MotionEventCompat.getX(event, pointerIndex);
                int currentY = (int) MotionEventCompat.getY(event, pointerIndex);
                int dx = mLastX - currentX;
                int dy = mLastY - currentY;

                //如果子view不处理滑动事件，就自己处理
                if (!mIsBeginDrag && Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop) {
                    mIsBeginDrag = true;
                    if (dy > 0) {
                        dy -= mScaledTouchSlop;
                    } else {
                        dy += mScaledTouchSlop;
                    }
                    if (dx > 0) {
                        dx -= mScaledTouchSlop;
                    } else {
                        dx += mScaledTouchSlop;
                    }
                }

                if (mIsBeginDrag) {
                    boolean b = overScrollBy(dx, dy, getScrollX(), getScrollY(), 0, getScrollRange(), 0, mOverScrollDistance, true);
                    mLastX = currentX;
                    mLastY = currentY;
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                onSecondPointerUp(event);

                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mIsBeginDrag = false;
                int index = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                float yVelocity = mVelocityTracker.getYVelocity(index);
                if (Math.abs(yVelocity) > mScaledMinimumFlingVelocity && canFling()) {
                    Log.d(TAG, "onTouchEvent() called with: " + "doFling");
                    doFling(-yVelocity);
                } else {
                    if (mOverScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        Log.d(TAG, "onTouchEvent() called with: " + "springBack");
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                mVelocityTracker.clear();
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                break;
            }
        }

        return true;
    }

    private boolean canFling() {
        if (getScrollY() < 0) {
            return (Math.abs(getScrollY())) < mOverScrollDistance;
        } else if (getScrollY() > 0) {
            return getScrollY() < getScrollRange() + mOverScrollDistance;
        }
        return mOverScrollDistance > 0;
    }

    private void doFling(float v) {
        Log.d(TAG + "DD", "yVelocity:" + v);
        mOverScroller.fling(
                getScrollX(),
                getScrollY(),
                0, (int) v,
                0, 0,
                0, getScrollRange(),
                0, mOverScrollDistance
        );
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void onSecondPointerDown(MotionEvent event) {
        int index = MotionEventCompat.getActionIndex(event);
        mActivePointerId = MotionEventCompat.getPointerId(event, index);
        mLastX = (int) MotionEventCompat.getX(event, index);
        mLastY = (int) MotionEventCompat.getY(event, index);
    }


    private void onSecondPointerUp(MotionEvent event) {
        int index = MotionEventCompat.getActionIndex(event);
        int pointerId = MotionEventCompat.getPointerId(event, index);
        if (mActivePointerId == pointerId) {
            int newIndex = index == 0 ? 1 : 0;
            mLastX = (int) MotionEventCompat.getX(event, newIndex);
            mLastY = (int) MotionEventCompat.getY(event, newIndex);
            mActivePointerId = MotionEventCompat.getPointerId(event, newIndex);
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        int childCount = getChildCount();
        if (childCount > 0) {
            View child = getChildAt(childCount - 1);
            scrollRange = Math.max(0,
                    child.getBottom() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }


    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mOverScroller.getCurrX();
            int y = mOverScroller.getCurrY();
            Log.d(TAG, "oldY:" + oldY + " oldX:" + oldX + " x:" + x + " y:" + y);
            if (oldX != x || oldY != y) {
                final int range = getScrollRange();
                int dx = x - oldX;
                int dy = y - oldY;
                overScrollBy(dx, dy, oldX, oldY, 0, range, 0, mOverScrollDistance, false);
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.d(TAG, "mOverScroller.isFinished():" + mOverScroller.isFinished());
        if (!mOverScroller.isFinished()) {
            super.scrollTo(scrollX, scrollY);
            if (clampedX || clampedY) {
                mOverScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, 0);
                Log.d(TAG, "onOverScrolled-->springBack");
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

}
