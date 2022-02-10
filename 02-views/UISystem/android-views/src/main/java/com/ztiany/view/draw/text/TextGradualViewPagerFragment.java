package com.ztiany.view.draw.text;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ztiany.recyclerview.viewpager2.PagerFragment;
import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

public class TextGradualViewPagerFragment extends Fragment {

    private static final String TAG = "TextGradualViewPager";

    private ViewPager mViewPager;
    private final List<ColorChangeTextView> mTabs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_fragment_gradual_color, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initEvents();
    }

    private void initView(@NonNull View view) {
        mViewPager = view.findViewById(R.id.id_viewpager);
        mTabs.add(view.findViewById(R.id.id_tab_01));
        mTabs.add(view.findViewById(R.id.id_tab_02));
        mTabs.add(view.findViewById(R.id.id_tab_03));
        mTabs.add(view.findViewById(R.id.id_tab_04));
    }

    private void initData() {
        final String[] titles = new String[]{"关注", "热点", "推荐", "长沙"};

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return PagerFragment.newInstance(titles[position]);
            }
        };

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    private void initEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ColorChangeTextView left = mTabs.get(position);
                    ColorChangeTextView right = mTabs.get(position + 1);
                    left.setDirection(ColorChangeTextView.DIRECTION_RIGHT);
                    right.setDirection(ColorChangeTextView.DIRECTION_LEFT);
                    Log.v(TAG, positionOffset + "");
                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
