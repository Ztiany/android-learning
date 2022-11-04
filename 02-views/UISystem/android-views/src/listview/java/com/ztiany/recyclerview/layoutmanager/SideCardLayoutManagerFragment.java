package com.ztiany.recyclerview.layoutmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ztiany.view.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SideCardLayoutManagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_fragment_side_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new SlideCardLayoutManager());

        List<SlideCardBean> cardBeans = SlideCardBean.initData();

        UniversalAdapter<SlideCardBean> adapter = new UniversalAdapter<SlideCardBean>(requireContext(), cardBeans, R.layout.rv_item_swipe_card) {
            @Override
            public void convert(ViewHolder viewHolder, SlideCardBean slideCardBean) {
                viewHolder.setText(R.id.tvName, slideCardBean.getName());
                viewHolder.setText(R.id.tvPercent, slideCardBean.getPosition() + "/" + mDatas.size());
                Glide.with(SideCardLayoutManagerFragment.this).load(slideCardBean.getUrl()).into((ImageView) viewHolder.getView(R.id.iv));
            }
        };

        rv.setAdapter(adapter);
        // 初始化数据
        CardConfig.initConfig(requireContext());
        SlideCallback slideCallback = new SlideCallback(rv, adapter, cardBeans);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(slideCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

}