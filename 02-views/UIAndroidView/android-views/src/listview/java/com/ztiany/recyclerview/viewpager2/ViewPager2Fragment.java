package com.ztiany.recyclerview.viewpager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考：https://mp.weixin.qq.com/s/cFeeg6RqvJksaS4ll_HdPQ。
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-12-25 17:04
 */
public class ViewPager2Fragment extends Fragment {

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
        return inflater.inflate(R.layout.rv_viewpager2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager2);
        viewPager2.setPageTransformer(new ScaleInTransformer());
        FragmentAdapter adapter = new FragmentAdapter(this);
        viewPager2.setAdapter(adapter);

        view.findViewById(R.id.btn_insert).setOnClickListener(v -> {
            mPagerNames.add(0, "Alice");
            adapter.notifyItemInserted(0);
        });

        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            mPagerNames.add(1, "Bob");
            adapter.notifyItemInserted(1);
        });

        view.findViewById(R.id.btn_remove).setOnClickListener(v -> {
            mPagerNames.remove(0);
            adapter.notifyItemRemoved(0);
        });

        view.findViewById(R.id.btn_refresh).setOnClickListener(v -> {
            adapter.notifyDataSetChanged();
        });
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
