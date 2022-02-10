package com.ztiany.recyclerview.diffutil;

import android.os.Bundle;

import androidx.recyclerview.widget.DiffUtil;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-22 16:29
 */
public class DiffItemCallback extends DiffUtil.ItemCallback<TestBean> {

    @Override
    public boolean areItemsTheSame(TestBean oldItem, TestBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(TestBean oldItem, TestBean newItem) {
        //如果有内容不同，就返回false
        return oldItem.getDes().equals(newItem.getDes()) && oldItem.getDrawableId() == newItem.getDrawableId();
    }

    @Override
    public Object getChangePayload(TestBean oldItem, TestBean newItem) {

        Bundle payload = new Bundle();

        if (!oldItem.getDes().equals(newItem.getDes())) {
            payload.putString(DataAdapter.KEY_DES, newItem.getDes());
        }

        if (oldItem.getDrawableId() != newItem.getDrawableId()) {
            payload.putInt(DataAdapter.KEY_IMAGE, newItem.getDrawableId());
        }

        if (payload.size() == 0) {//如果没有变化 就传空
            return null;
        }

        return payload;
    }


}
