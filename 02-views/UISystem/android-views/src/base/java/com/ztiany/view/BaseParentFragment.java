package com.ztiany.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-11-08 15:27
 */
public class BaseParentFragment extends Fragment {

    protected static class FragInfo {

        private final String name;
        private final Class<? extends Fragment> clazz;
        private final Bundle args;
        private Fragment fragment;

        public FragInfo(String name, Class<? extends Fragment> clazz, Bundle args) {
            this.name = name;
            this.clazz = clazz;
            this.args = args;
            fragment = null;
        }

        public FragInfo(String name, Class<? extends Fragment> clazz) {
            this.name = name;
            this.clazz = clazz;
            this.args = null;
            fragment = null;
        }

        public FragInfo(String name, Fragment fragment) {
            this.name = name;
            this.clazz = null;
            this.args = null;
            this.fragment = fragment;
        }

    }

    private List<FragInfo> mFragInfoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFragInfoList = new ArrayList<>();
        onFillChildrenUp(mFragInfoList);
    }

    protected void onFillChildrenUp(List<FragInfo> fragInfoList) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragInfo fragInfo = mFragInfoList.get(0);
        if (fragInfo != null) {
            showFragment(fragInfo);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (FragInfo fragInfo : mFragInfoList) {
            menu.add(fragInfo.name)
                    .setOnMenuItemClickListener(item -> {
                        showFragment(fragInfo);
                        return true;
                    });
        }
    }

    private void showFragment(FragInfo fragInfo) {
        if (fragInfo.fragment == null) {
            assert fragInfo.clazz != null;
            fragInfo.fragment = Fragment.instantiate(requireContext(), fragInfo.clazz.getName(), fragInfo.args);
        }
        replaceChild(fragInfo.fragment);
    }

    protected void replaceChild(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment, fragment.getClass().getName())
                .commit();
    }

}
