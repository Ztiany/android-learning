package com.xiangxue.nestedscroll.e_perfect_nestedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xiangxue.common.fragment.NestedLogRecyclerView;
import com.xiangxue.common.utils.FlingHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class NestedScrollLayout extends NestedScrollView {

    private View topView;
    private ViewGroup contentView;

    private static final String TAG = "NestedScrollLayout";

    public NestedScrollLayout(Context context) {
        this(context, null);
        init();
    }

    public NestedScrollLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public NestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        init();
    }

    public NestedScrollLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private FlingHelper mFlingHelper;

    int totalDy = 0;

    /**
     * 用于判断RecyclerView是否在fling
     */
    boolean isStartFling = false;

    /**
     * 记录当前滑动的y轴加速度
     */
    private int velocityY = 0;

    private void init() {
        mFlingHelper = new FlingHelper(getContext());
        setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isStartFling) {
                    totalDy = 0;
                    isStartFling = false;
                }
                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }
                if (scrollY == (getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                    dispatchChildFling();
                }
                //在RecyclerView fling情况下，记录当前RecyclerView在y轴的偏移
                totalDy += scrollY - oldScrollY;
            }
        });
    }

    private void dispatchChildFling() {
        if (velocityY != 0) {
            double splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY);
            if (splineFlingDistance > totalDy) {
                childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - (double) totalDy));
            }
        }
        totalDy = 0;
        velocityY = 0;
    }

    private void childFling(int velY) {
        RecyclerView childRecyclerView = getChildRecyclerView(contentView);
        if (childRecyclerView != null) {
            childRecyclerView.fling(0, velY);
        }
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        if (velocityY <= 0) {
            this.velocityY = 0;
        } else {
            isStartFling = true;
            this.velocityY = velocityY;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = ((ViewGroup) getChildAt(0)).getChildAt(0);
        contentView = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = contentView.getLayoutParams();
        lp.height = getMeasuredHeight();
        contentView.setLayoutParams(lp);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.i("NestedScrollLayout", getScrollY() + "::onNestedPreScroll::" + topView.getMeasuredHeight());
        // 向上滑动。若当前 top view 可见，需要将 top view 滑动至不可见
        boolean hideTop = dy > 0 && getScrollY() < topView.getMeasuredHeight();
        if (hideTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    private RecyclerView getChildRecyclerView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof RecyclerView && view.getClass() == NestedLogRecyclerView.class) {
                return (RecyclerView) viewGroup.getChildAt(i);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                RecyclerView childRecyclerView = getChildRecyclerView((ViewGroup) viewGroup.getChildAt(i));
                if (childRecyclerView != null) {
                    return childRecyclerView;
                }
            }
            continue;
        }
        return null;
    }

}
