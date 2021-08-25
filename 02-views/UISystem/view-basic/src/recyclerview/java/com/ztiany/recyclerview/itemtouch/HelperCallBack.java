package com.ztiany.recyclerview.itemtouch;

import android.util.Log;

import com.ztiany.recyclerview.itemtouch.help.ItemTouchHelperAdapter;
import com.ztiany.recyclerview.itemtouch.help.ItemTouchHelperViewHolder;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


class HelperCallBack extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mItemTouchHelperAdapter;

    private static final String TAG = HelperCallBack.class.getSimpleName();

    HelperCallBack(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        mItemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    /*
        ItemTouchHelper可以让你轻易得到一个事件的方向。你需要重写 getMovementFlags() 方法来指定可以支持的拖放和滑动的方向。
        使用 helperItemTouchHelper.makeMovementFlags(int, int) 来构造返回的flag。注：上下为拖动（drag），左右为滑动（swipe）。
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            //拖动标志-拖动排序，根据标志判断是否允许某个方向上的拖动。
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;//不允许滑动删除
            return makeMovementFlags(dragFlags, swipeFlags);
        } else if (layoutManager instanceof LinearLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖动标志-拖动排序，这里返回的UP | DOWN 表示只允许上下拖动
            /*
            ItemTouchHelper.START | ItemTouchHelper.END的注释为：
                    依赖于layoutManager的布局方向，理解就是与布局方向垂直的方法，用来将 item swipe 移除
                    Horizontal end direction. Resolved to LEFT or RIGHT depending on RecyclerView's layout direction. Used for swipe & drag control.
             */
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;//滑动标志位-滑动删除：这里返回 START | END 表示允许 左右滑动
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mItemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //当 Item 的选择状态改变时我们可以在这里通知其改变状态。

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
            }
        }

        if (viewHolder == null) {
            Log.d(TAG, " viewHolder == null  " + (actionState == 0 ? "ACTION_STATE_IDLE" : actionState == 1 ? "ACTION_STATE_SWIPE" : "ACTION_STATE_DRAG"));
            return;
        }

        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_DRAG:
                Log.d(TAG, viewHolder.getAdapterPosition() + "  " + "ACTION_STATE_DRAG");
                break;
            case ItemTouchHelper.ACTION_STATE_IDLE:
                Log.d(TAG, viewHolder.getAdapterPosition() + "  " + "ACTION_STATE_IDLE");
                break;
            case ItemTouchHelper.ACTION_STATE_SWIPE:
                Log.d(TAG, viewHolder.getAdapterPosition() + "  " + "ACTION_STATE_SWIPE");
                break;
            default:
                break;
        }
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }

    /*
    ItemTouchHelper可以用于没有滑动的拖动操作（或者反过来），你必须指明你到底要支持哪一种。
    要支持长按RecyclerView item进入拖动操作，你必须在isLongPressDragEnabled()方法中返回true。
    或者，也可以调用ItemTouchHelper.startDrag(RecyclerView.ViewHolder) 方法来开始一个拖动
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

}
