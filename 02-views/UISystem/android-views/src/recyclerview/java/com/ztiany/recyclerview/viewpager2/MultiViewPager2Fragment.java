package com.ztiany.recyclerview.viewpager2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

public class MultiViewPager2Fragment extends Fragment {

    private final List<String> mPagerNames = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerNames.add("Alien");
        mPagerNames.add("Biden");
        mPagerNames.add("C Roy");
        mPagerNames.add("Drill");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_viewpager2_multi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager2);

        RecyclerView internalRV = (RecyclerView) viewPager2.getChildAt(0);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        internalRV.setPadding(margin * 2, margin * 2, margin * 2, margin * 2);
        internalRV.setClipToPadding(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new ScaleInTransformer());
        compositePageTransformer.addTransformer(new MarginPageTransformer(margin));
        viewPager2.setPageTransformer(compositePageTransformer);
        FragmentAdapter adapter = new FragmentAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    private class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return PagerFragment.newInstance(mPagerNames.get(position));
        }

        @Override
        public int getItemCount() {
            return mPagerNames.size();
        }

        @Override
        public long getItemId(int position) {
            return mPagerNames.get(position).hashCode();
        }

    }

}