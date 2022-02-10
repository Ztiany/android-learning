package com.ztiany.recyclerview.diffutil;

import android.os.Bundle;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

class DiffCallback extends DiffUtil.Callback {

    private List<TestBean> mOldData, mNewData;//看名字

    DiffCallback(List<TestBean> mOldData, List<TestBean> mNewData) {
        this.mOldData = mOldData;
        this.mNewData = mNewData;
    }


    //返回老数据集
    @Override
    public int getOldListSize() {
        return mOldData != null ? mOldData.size() : 0;
    }

    //返回新数据集
    @Override
    public int getNewListSize() {
        return mNewData != null ? mNewData.size() : 0;
    }

    /*
        被DiffUtil调用，用来判断 两个对象是否是相同的Item。例如，如果你的Item有唯一的id字段，
        这个方法就 判断id是否相等。本例判断name字段是否一致
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getId() == mNewData.get(newItemPosition).getId();
    }

    /*
    被DiffUtil调用，用来检查 两个item是否含有相同的数据，DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
    DiffUtil 用这个方法替代equals方法去检查是否相等。所以你可以根据你的UI去改变它的返回值
    例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
    这个方法仅仅在areItemsTheSame()返回true时，才调用。
     */
    @Override
    @SuppressWarnings("all")
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TestBean beanOld = mOldData.get(oldItemPosition);
        TestBean beanNew = mNewData.get(newItemPosition);
        if (!beanOld.getDes().equals(beanNew.getDes())) {
            return false;//如果有内容不同，就返回false
        }
        if (beanOld.getDrawableId() != beanNew.getDrawableId()) {
            return false;//如果有内容不同，就返回false
        }
        return true; //默认两个data内容是相同的
    }


    /*
    当{@link #areItemsTheSame(int, int)} 返回true，且{@link #areContentsTheSame(int, int)} 返回false时，DiffUtils会回调此方法，
    去得到这个Item（有哪些）改变的payload。

    例如，如果你用RecyclerView配合DiffUtils，你可以返回  这个Item改变的那些字段，
    {@link  androidx.recyclerview.widget.RecyclerView.ItemAnimator ItemAnimator} 可以用那些信息去执行正确的动画

    默认的实现是返回null
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // 定向刷新中的部分更新，效率最高

        TestBean oldBean = mOldData.get(oldItemPosition);
        TestBean newBean = mNewData.get(newItemPosition);

        Bundle payload = new Bundle();

        if (!oldBean.getDes().equals(newBean.getDes())) {
            payload.putString(DataAdapter.KEY_DES, newBean.getDes());
        }

        if (oldBean.getDrawableId() != newBean.getDrawableId()) {
            payload.putInt(DataAdapter.KEY_IMAGE, newBean.getDrawableId());
        }

        if (payload.size() == 0) {//如果没有变化 就传空
            return null;
        }

        return payload;
    }
}

