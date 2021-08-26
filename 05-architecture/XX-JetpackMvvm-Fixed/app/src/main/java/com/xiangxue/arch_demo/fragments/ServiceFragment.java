package com.xiangxue.arch_demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiangxue.arch_demo.R;
import com.xiangxue.arch_demo.databinding.FragmentOthersBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * Created by Vishal Patolia on 18-Feb-18.
 */
public class ServiceFragment extends Fragment {

    FragmentOthersBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_others, container, false);
        mBinding.homeTxtTitle.setText(getString(R.string.menu_services));
        return mBinding.getRoot();
    }

}
