package com.ztiany.recyclerview.layoutmanager;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

/**
 * 自定义LayoutManager的基本流程
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-29 15:44
 */
class CustomLinearLayoutManager extends LayoutManager {

    private OrientationHelper mOrientationHelper;

    CustomLinearLayoutManager() {
        mOrientationHelper = OrientationHelper.createOrientationHelper(this, OrientationHelper.VERTICAL);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //在初始化布局以及adapter数据发生改变（或更换adapter）的时候调用。所以我们在这个方法中对我们的item进行测量以及初始化。
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item
        if (getItemCount() <= 0) {
            detachAndScrapAttachedViews(recycler);//分离所有的子view
            return;
        }
        // 跳过preLayout，preLayout用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);//先分离所有的子view
        recycleAndFillItems(recycler, state, 0);
    }

    /**
     * 回收不需要的Item，并且将需要显示的Item从缓存中取出
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        // 跳过preLayout，preLayout用于支持动画
        if (state.isPreLayout()) {
            return;
        }

        //第一步回收
        recycleAllChildren(recycler, dy);

        //第二步 填充view
        int topEdge = mOrientationHelper.getStartAfterPadding();
        int bottomEdge = mOrientationHelper.getEndAfterPadding();

        int startPosition = 0;
        int startOffset = -1;
        int scrapWidth, scrapHeight;
        View scrap;

        if (dy >= 0) {//上滑动
            if (getChildCount() > 0) {//如果存在view，就找到最后那个，继续填充
                View lastChild = getChildAt(getChildCount() - 1);
                startOffset = getDecoratedBottom(lastChild);
                startPosition = getPosition(lastChild) + 1;
            }
            for (int i = startPosition; i < getItemCount() && startOffset < bottomEdge + dy; i++) {
                scrap = recycler.getViewForPosition(i);
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, 0, startOffset, scrapWidth, startOffset + scrapHeight);
                startOffset += scrapHeight;
            }
        } else {

            //下滑动
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                startOffset = getDecoratedTop(firstView);
                startPosition = getPosition(firstView) - 1; //前一个View的position
            }

            for (int i = startPosition; i >= 0 && startOffset > topEdge + dy; i--) {
                scrap = recycler.getViewForPosition(i);
                addView(scrap, 0);
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, 0, startOffset - scrapHeight, scrapWidth, startOffset);
                startOffset -= scrapHeight;
            }
        }
    }

    private void recycleAllChildren(RecyclerView.Recycler recycler, int dy) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        if (dy >= 0) {//上滑动，顶部的view可能被滑出屏幕，所以检测是否要被回收
            int topEdge = mOrientationHelper.getStartAfterPadding();
            int fixIndex = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i + fixIndex);
                int decoratedBottom = getDecoratedBottom(child);
                if (decoratedBottom - dy < topEdge) {//滑出了屏幕就回收
                    removeAndRecycleView(child, recycler);
                    fixIndex--;//修复索引
                }
            }
        } else {
            //下滑动，底部的view可能被滑出屏幕，所以检测是否要被回收
            int bottomEdge = mOrientationHelper.getEndAfterPadding();
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                int decoratedTop = getDecoratedTop(child);
                if (decoratedTop - dy > bottomEdge) {
                    removeAndRecycleView(child, recycler);
                }
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //往下拉dy<0，往上拉dy>0，verticalScrollOffset的范围在 0 到 totalHeight之间
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        int delta = -dy;
        View child;
        if (dy > 0) {
            //If we've reached the last item, enforce limits
            if (getPosition(getChildAt(getChildCount() - 1)) == getItemCount() - 1) {
                child = getChildAt(getChildCount() - 1);
                int end = mOrientationHelper.getEndAfterPadding();
                int decoratedBottom = getDecoratedBottom(child);
                if (decoratedBottom - dy <= end) {
                    delta = -(decoratedBottom - end);
                }
            }
        } else {
            if (getPosition(getChildAt(0)) == 0) {
                child = getChildAt(0);
                int start = mOrientationHelper.getStartAfterPadding();
                int decoratedTop = getDecoratedTop(child);
                if (decoratedTop + dy <= start) {
                    delta = -(decoratedTop - start);
                }
            }
        }
        recycleAndFillItems(recycler, state, -delta);//根据将要滑动的距离填充新的子view和移除不再需要的view
        offsetChildrenVertical(delta);//调整子view的偏移
        return -delta;
    }

    @Override
    public void scrollToPosition(int position) {
        // TODO: 17.10.12
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        // TODO: 17.10.12
    }

}
