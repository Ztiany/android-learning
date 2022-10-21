package com.ztiany.recyclerview.itemtouch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;
import com.ztiany.recyclerview.itemtouch.help.OnStartDragListener;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class GridFragment extends Fragment {

    private ItemTouchHelper mTouchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_common_fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(LayoutInflater.from(getContext()), new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mTouchHelper.startDrag(viewHolder);
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        mTouchHelper = new ItemTouchHelper(new HelperCallBack(recyclerAdapter));
        mTouchHelper.attachToRecyclerView(recyclerView);
    }

}
