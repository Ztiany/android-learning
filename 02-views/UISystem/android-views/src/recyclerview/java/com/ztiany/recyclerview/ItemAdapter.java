package com.ztiany.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class ItemAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<Item> mItems;

    ItemAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button button = new AppCompatButton(mContext);
        button.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RecyclerView.ViewHolder(button) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        Button button = (Button) holder.itemView;
        button.setText(item.mName);
        if (Activity.class.isAssignableFrom(item.mClazz)) {
            button.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, item.mClazz)));
        } else {
            button.setOnClickListener(v -> mContext.startActivity(RvContentActivity.getLaunchIntent(mContext, item.mName, item.mClazz)));
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

}
