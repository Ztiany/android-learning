package com.ztiany.recyclerview.diffutil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztiany.view.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-25 16:46
 */
public class AsyncListDifferDataAdapter extends RecyclerView.Adapter<AsyncListDifferDataAdapter.DataHolder> {

    public static final String KEY_IMAGE = "key_image";
    public static final String KEY_DES = "key_des";

    private AsyncListDiffer<TestBean> mAsyncListDiffer;

    AsyncListDifferDataAdapter() {
        mAsyncListDiffer = new AsyncListDiffer<>(this, new DiffItemCallback());
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_diff_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        TestBean testBean = getTestBeans().get(position);

        holder.mDes.setText(testBean.getDes());
        holder.mId.setText(String.valueOf(testBean.getId()));
        int drawableId = testBean.getDrawableId();
        if (drawableId == 0) {
            holder.mImage.setImageDrawable(null);
        } else {
            holder.mImage.setImageResource(drawableId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle payload = (Bundle) payloads.get(0);
            for (String key : payload.keySet()) {
                switch (key) {
                    case KEY_DES:
                        //这里可以用payload里的数据，不过data也是新的 也可以用
                        holder.mDes.setText(payload.getString(key));
                        break;
                    case KEY_IMAGE:
                        int drawableId = payload.getInt(key);
                        if (drawableId == 0) {
                            holder.mImage.setImageDrawable(null);
                        } else {
                            holder.mImage.setImageResource(drawableId);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        List<TestBean> beans = getTestBeans();
        return beans == null ? 0 : beans.size();
    }

    static class DataHolder extends RecyclerView.ViewHolder {

        private TextView mDes;
        private TextView mId;
        private ImageView mImage;

        DataHolder(View itemView) {
            super(itemView);
            mDes = itemView.findViewById(R.id.tv_des);
            mId = itemView.findViewById(R.id.tv_id);
            mImage = itemView.findViewById(R.id.iv_image);
        }
    }

    public List<TestBean> getTestBeans() {
        return mAsyncListDiffer.getCurrentList();
    }

    public void submitList(List<TestBean> testBeans) {
        mAsyncListDiffer.submitList(testBeans);
    }

}
