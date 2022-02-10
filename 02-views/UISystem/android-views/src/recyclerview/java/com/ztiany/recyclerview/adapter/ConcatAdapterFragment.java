package com.ztiany.recyclerview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztiany.recyclerview.common.adapter.recycler.RecyclerAdapter;
import com.ztiany.recyclerview.common.adapter.recycler.SmartViewHolder;

import java.util.Arrays;
import java.util.List;

public class ConcatAdapterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setUpRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ConcatAdapter adapter = new ConcatAdapter(
                new Adapter1(requireContext(), Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N")),
                new Adapter2(requireContext(), Arrays.asList("H", "I", "J", "K", "L", "M", "N", "A", "B", "C", "D", "E", "F", "G"))
        );
        recyclerView.setAdapter(adapter);
    }

    private static class Adapter1 extends RecyclerAdapter<String, SmartViewHolder> {

        public Adapter1(@NonNull Context context, List<String> data) {
            super(context, data);
        }

        @NonNull
        @Override
        public SmartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatTextView itemView = new AppCompatTextView(parent.getContext());
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.setGravity(Gravity.CENTER_VERTICAL);
            itemView.setBackgroundColor(Color.BLUE);
            itemView.setTextColor(Color.WHITE);
            itemView.setTextSize(20);
            return new SmartViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull SmartViewHolder viewHolder, int position) {
            ((TextView) (viewHolder.itemView)).setText(getItem(position));
        }
    }

    private static class Adapter2 extends RecyclerAdapter<String, SmartViewHolder> {

        public Adapter2(@NonNull Context context, List<String> data) {
            super(context, data);
        }

        @NonNull
        @Override
        public SmartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatTextView itemView = new AppCompatTextView(parent.getContext());
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            itemView.setBackgroundColor(Color.RED);
            itemView.setTextColor(Color.WHITE);
            itemView.setTextSize(30);
            return new SmartViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull SmartViewHolder viewHolder, int position) {
            ((TextView) (viewHolder.itemView)).setText(getItem(position));
        }
    }

}
