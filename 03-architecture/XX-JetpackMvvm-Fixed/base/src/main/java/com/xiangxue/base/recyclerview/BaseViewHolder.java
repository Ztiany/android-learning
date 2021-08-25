package com.xiangxue.base.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiangxue.base.customview.IBaseCustomView;
import com.xiangxue.base.customview.BaseCustomViewModel;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private IBaseCustomView itemView;
    public BaseViewHolder(@NonNull IBaseCustomView itemView) {
        super((View) itemView);
        this.itemView = itemView;
    }

    public void bind(BaseCustomViewModel viewModel){
        this.itemView.setData(viewModel);
    }
}
