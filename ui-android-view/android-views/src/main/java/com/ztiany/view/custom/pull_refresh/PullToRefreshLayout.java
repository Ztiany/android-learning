package com.ztiany.view.custom.pull_refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.ztiany.view.utils.UnitConverter;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-14 15:13
 */
public class PullToRefreshLayout extends FrameLayout {


    private static final String TAG = PullToRefreshLayout.class.getSimpleName();

    private final State mState = new State();

    private TextView mHeaderView;//头部
    private View mContentView;//内容

    private int mScaledTouchSlop;// 最小滑动响应距离
    private boolean mIsRefreshable = true;

    private int mInitActivePointerId;
    private int mActivePointerId;

    private float mInitDownY;
    private float mLastMotionY;

    private boolean mProcessingEvent;

    private ScrollChecker mScrollChecker;

    private class State {

        private static final int HIDE = 0;// 隐藏的状态
        private static final int PULL_TO_REFRESH = 1;// 下拉刷新的状态
        private static final int RELEASE_TO_REFRESH = 2; // 松开刷新的状态
        private static final int REFRESHING = 3;// 正在刷新的状态
        private static final int HIDING = 4;// 正在隐藏的状态

        private int mCurrentState = HIDE;// 当前状态

        private boolean ignoreEvent() {
            if ((mCurrentState == REFRESHING || mCurrentState == RELEASE_TO_REFRESH || mCurrentState == HIDING)) {
                // 正在刷新，正在释放，正在隐藏头部都不处理事件，并且不分发下去
                return true;
            }
            return false;
        }

        boolean isRefreshing() {
            return REFRESHING == mCurrentState;
        }

        void setReleaseToRefresh() {
            mCurrentState = RELEASE_TO_REFRESH;
        }

        void setPullToRefresh() {
            mCurrentState = PULL_TO_REFRESH;
        }

        boolean isReleaseToRefresh() {
            return mCurrentState == RELEASE_TO_REFRESH;
        }

        boolean isPullToRefresh() {
            return mCurrentState == PULL_TO_REFRESH;
        }
    }


    public PullToRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        mScrollChecker = new ScrollChecker();
        addHeader();
    }

    private void addHeader() {
        mHeaderView = new TextView(getContext());
        mHeaderView.setBackgroundColor(Color.GRAY);
        mHeaderView.setTextColor(Color.WHITE);
        mHeaderView.setText("我是头部");
        int padding = UnitConverter.dpToPx(15);
        mHeaderView.setPadding(padding, padding, padding, padding);
        mHeaderView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        mHeaderView.setGravity(Gravity.CENTER);
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mHeaderView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(1);
        if (mContentView == null) {
            throw new IllegalStateException("you need provide a content view");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int measuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.layout(left, -measuredHeight, right, top);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 禁止下拉刷新，直接把事件分发
        if (!mIsRefreshable) {
            return super.dispatchTouchEvent(ev);
        }
//        if (mState.ignoreEvent()) {
//            return true;
//        }

        int actionMasked = MotionEventCompat.getActionMasked(ev);

        switch (actionMasked) {

            case MotionEvent.ACTION_DOWN:
                // 记录响应的手指
                mActivePointerId = ev.getPointerId(0);//刚开始只有一个手指
                // 记录初始Y坐标
                mInitDownY = mLastMotionY = ev.getY(0);
                mInitActivePointerId = mActivePointerId;
                break;

            case MotionEvent.ACTION_POINTER_DOWN://第二个手指按下，更换手指
                int actionIndex = MotionEventCompat.getActionIndex(ev);
                //容错处理
                if (actionIndex < 0) {
                    return super.dispatchTouchEvent(ev);
                }
                mActivePointerId = ev.getPointerId(actionIndex);
                mLastMotionY = ev.getY(actionIndex);
                break;

            case MotionEvent.ACTION_POINTER_UP://第二个手指抬起，更换手指
                int upIndex = MotionEventCompat.getActionIndex(ev);
                int pointerId = ev.getPointerId(upIndex);
                if (pointerId != mActivePointerId) {
                    // 抬起手指就是之前控制滑动手指，切换其他手指响应
                    final int newPointerIndex = upIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId));
                break;

            case MotionEvent.ACTION_MOVE://处理手指移动
                //容错处理
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return super.dispatchTouchEvent(ev);
                }

                float y;
                try {
                    y = ev.getY(ev.findPointerIndex(mActivePointerId));
                } catch (Exception e) {
                    mActivePointerId = ev.getPointerId(MotionEventCompat.getActionIndex(ev));
                    mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId));
                    y = ev.getY(ev.findPointerIndex(mActivePointerId));
                }

                float yOffset = y - mLastMotionY;
                mLastMotionY = y;

                if ((yOffset < 0 && isHeaderShow()) || (yOffset > 0 && !canChildScrollUp(mContentView))) {//自己处理

                    // 滑的总距离
                    float totalDistanceY = Math.abs(y - mInitDownY);
                    if (!mProcessingEvent && totalDistanceY <= mScaledTouchSlop) {
                        // 下拉时，优化滑动逻辑，不要稍微一点位移就响应
                        return super.dispatchTouchEvent(ev);
                    }
                    move(yOffset);
                    mProcessingEvent = true;

                } else if (mProcessingEvent && yOffset < 0) {
                    mProcessingEvent = false;
                    ev.setAction(MotionEvent.ACTION_DOWN);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isHeaderShow()) {
                    if (actionMasked == MotionEvent.ACTION_CANCEL) {
                        // 取消的话强制不能刷新，状态改为下拉刷新，接下来autoScrollHeader就会隐藏头部
                        mState.setPullToRefresh();
                    }
                    autoScrollHeader();
                }
                reset();
                break;
        }

        if (mProcessingEvent) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }

        return super.dispatchTouchEvent(ev);
    }

    private void autoScrollHeader() {
        // 处理抬起事件
        if (mState.isReleaseToRefresh()) {
            mScrollChecker.tryToScrollTo(0, 300);
        } else if (mState.isPullToRefresh()) {
            mScrollChecker.tryToScrollTo(-mHeaderView.getMeasuredHeight(), 300);
        } else if (mState.isRefreshing()) {
            mScrollChecker.tryToScrollTo(0, 300);
        }
    }

    private void reset() {
        mProcessingEvent = false;
    }

    private void move(float yOffset) {
        mHeaderView.offsetTopAndBottom((int) yOffset);
        mContentView.offsetTopAndBottom((int) yOffset);

        if (mState.isRefreshing()) {
            // 之前还在刷新状态，继续维持刷新状态
            mHeaderView.setText("正在刷新...");
            return;
        }
        if (mHeaderView.getTop() > 0) {
            // 大于mHeaderHeight / 2时可以刷新了
            mHeaderView.setText("可以释放刷新...");
            mState.setReleaseToRefresh();
        } else {
            // 下拉状态
            mHeaderView.setText("正在下拉...");
            mState.setPullToRefresh();
        }
    }

    private boolean isHeaderShow() {
        return mHeaderView.getTop() > -mHeaderView.getHeight();
    }

    @SuppressLint("ObsoleteSdkInt")
    public boolean canChildScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(view, -1);
        }
    }


    private class ScrollChecker implements Runnable {

        private int mLastFlingY;
        private Scroller mScroller;
        private boolean mIsRunning = false;
        private int mStart;
        private int mTo;

        private ScrollChecker() {
            mScroller = new Scroller(getContext());
        }

        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int curY = mScroller.getCurrY();
            int deltaY = Math.abs(curY) - Math.abs(mLastFlingY);
            if (!finish) {
                mLastFlingY = curY;
                move(-deltaY);
                post(this);
            } else {
                finish();
            }
        }

        private void finish() {
            reset();
        }

        private void reset() {
            mIsRunning = false;
            mLastFlingY = 0;
            removeCallbacks(this);
        }

        private void destroy() {
            reset();
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
        }

        public void abortIfWorking() {
            if (mIsRunning) {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                reset();
            }
        }

        void tryToScrollTo(int to, int duration) {
            if (mHeaderView.getTop() == to) {
                return;
            }
            mStart = mHeaderView.getTop();
            mTo = to;
            int distance = to - mStart;
            removeCallbacks(this);
            mLastFlingY = 0;
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, distance, duration);
            post(this);
            mIsRunning = true;
        }
    }
}
