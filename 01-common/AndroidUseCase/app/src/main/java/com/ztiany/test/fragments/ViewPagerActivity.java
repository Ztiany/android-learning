package com.ztiany.test.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class ViewPagerActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(View.generateViewId());
        setContentView(viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new TestFragment1();
                } else if (position == 1) {
                    return new TestFragment1();
                } else {
                    return new TestFragment1();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

    }

}