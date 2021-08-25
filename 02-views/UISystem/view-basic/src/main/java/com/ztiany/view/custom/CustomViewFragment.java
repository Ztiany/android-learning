package com.ztiany.view.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ztiany.view.custom.custom_viewgroup.SquareEnhanceLayout;
import com.ztiany.view.custom.custom_viewpager.HScrollLayoutBuilder;
import com.ztiany.view.custom.ruler.RulerView;
import com.ztiany.view.custom.surfaceview.LoadingView;
import com.ztiany.view.custom.views.LockPatternView;
import com.ztiany.view.custom.views.Ring;
import com.ztiany.view.custom.views.SurfaceViewSinFun;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-05 15:44
 */
public class CustomViewFragment extends Fragment {

    private List<View> mViewList = new ArrayList<>();

    private FrameLayout mFrameLayout;

    private String[] titles = {
            "水平滑动",
            "Surface Loading",
            "宫格锁",
            "圆环",
            "尺子",
            "正方形布局",
            "SurfaceViewSinFun"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addViews();
    }

    private void addViews() {
        mViewList.add(HScrollLayoutBuilder.buildHScrollLayout(getContext()));
        mViewList.add(new LoadingView(getContext()));
        mViewList.add(new LockPatternView(getContext()));
        mViewList.add(new Ring(getContext(), null));
        mViewList.add(new RulerView(getContext()));
        mViewList.add(new SquareEnhanceLayout(getContext()));
        mViewList.add(new SurfaceViewSinFun(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mFrameLayout != null ? mFrameLayout : (mFrameLayout = new FrameLayout(requireContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mViewList.get(0), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem.OnMenuItemClickListener onMenuItemClickListener = item -> {
            mFrameLayout.removeAllViews();
            mFrameLayout.addView(mViewList.get(item.getItemId()), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return true;
        };

        for (int index = 0; index < titles.length; index++) {
            menu.add(Menu.NONE, index, index, titles[index]).setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

}