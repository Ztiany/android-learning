package com.ztiany.recyclerview.adapter.recycler;

import android.view.View;

import com.ztiany.recyclerview.adapter.ItemHelper;


public class SmartViewHolder extends ViewHolder {

    @SuppressWarnings("all")
    protected final ItemHelper mHelper;

    public SmartViewHolder(View itemView) {
        super(itemView);
        mHelper = new ItemHelper(itemView);
    }

    public ItemHelper helper() {
        return mHelper;
    }

}
