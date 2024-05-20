package com.ztiany.view.custom.view_pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ztiany.view.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * http://blog.csdn.net/lmj623565791/article/details/51339751
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-29 11:02
 */
public class ViewPagerFragment extends Fragment {

    private int[] imgRes = {R.drawable.img_scenery_a, R.drawable.img_scenery_b, R.drawable.img_scenery_c, R.drawable.img_scenery_d};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.viewpager_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(view);
    }

    private void setupViewPager(View view) {
        ViewPager viewPager = view.findViewById(R.id.id_viewpager);
        //设置Page间间距
        viewPager.setPageMargin(20);
        //设置缓存的页面数量
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView view = new ImageView(getContext());
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setImageResource(imgRes[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return imgRes.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        });
    }

}