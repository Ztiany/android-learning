package com.ztiany.recyclerview.item_decoraion_index;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 使用 ItemDecoration Item分组粘性头部，参考http://blog.csdn.net/zxt0601。
 */
public class ItemDecorationIndexFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mRecyclerView == null ? mRecyclerView = new RecyclerView(getContext()) : mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter());
        mRecyclerView.addItemDecoration(new StickySectionDecoration(getContext(), new StickySectionDecoration.GroupInfoCallback() {
            @Override
            public void fillGroupInfo(StickySectionDecoration.GroupInfo groupInfo, int position) {
                groupInfo.setTitle("group " + position / 5);
                groupInfo.setPosition(position % 5);
                groupInfo.setGroupLength(5);
            }
        }));
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new AppCompatTextView(getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.BLACK);
            int padding = UnitConverter.dpToPx(10);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextSize(24);
            textView.getPaint().setFakeBoldText(true);
            textView.setGravity(Gravity.CENTER);
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("I am Item , position = " + position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

}
