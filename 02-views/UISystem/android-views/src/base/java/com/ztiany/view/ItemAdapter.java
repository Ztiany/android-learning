package com.ztiany.view;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<Item> mItems;

    public ItemAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button button = new MaterialButton(mContext);
        button.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RecyclerView.ViewHolder(button) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        Button button = (Button) holder.itemView;
        button.setText(item.mName);
        button.setOnClickListener(v -> {
            if (AppCompatActivity.class.isAssignableFrom(item.mClazz)) {
                mContext.startActivity(new Intent(mContext, item.mClazz));
            } else {
                mContext.startActivity(ContentActivity.getLaunchIntent(mContext, item.mName, item.mClazz));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

}
