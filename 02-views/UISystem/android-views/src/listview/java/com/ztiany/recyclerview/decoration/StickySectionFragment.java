package com.ztiany.recyclerview.decoration;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztiany.view.utils.UnitConverter;

/**
 * 使用 ItemDecoration Item分组粘性头部，参考：http://blog.csdn.net/zxt0601。
 */
public class StickySectionFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mRecyclerView == null ? mRecyclerView = new RecyclerView(requireContext()) : mRecyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter());
        mRecyclerView.addItemDecoration(new StickySectionDecoration(requireContext(), (groupInfo, position) -> {
            groupInfo.setTitle("group " + position / 5);
            groupInfo.setPosition(position % 5);
            groupInfo.setGroupLength(5);
        }));
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new AppCompatTextView(requireContext());
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
