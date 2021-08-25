package com.xiangxue.common.recyclerview;

import android.view.View;

import com.xiangxue.common.customview.BaseCustomViewModel;
import com.xiangxue.common.customview.ICustomView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    ICustomView view;

    public BaseViewHolder(ICustomView view) {
        super((View) view);
        this.view = view;
    }

    public void bind(@NonNull BaseCustomViewModel item) {
        view.setData(item);
    }

}