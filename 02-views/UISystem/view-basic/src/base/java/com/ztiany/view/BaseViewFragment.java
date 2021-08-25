package com.ztiany.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-11-08 15:40
 */
public class BaseViewFragment extends Fragment {

    private static final String L_ID = "lid";

    public static BaseViewFragment newInstance(int layoutId) {
        Bundle args = new Bundle();
        args.putInt(L_ID, layoutId);
        BaseViewFragment fragment = new BaseViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        assert arguments != null;
        return inflater.inflate(arguments.getInt(L_ID), container, false);
    }

}
