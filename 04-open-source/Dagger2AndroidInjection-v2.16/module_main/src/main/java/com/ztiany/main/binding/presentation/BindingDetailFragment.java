package com.ztiany.main.binding.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.main.databinding.ModuleMainFragmentBindingDetailBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 15:11
 */
public class BindingDetailFragment extends Fragment {

    private String mId;
    private static final String ID_KEY = "id_key";

    @Inject
    BindingDetailViewModel mDetailViewModel;

    public static Fragment newInstance(String id) {
        BindingDetailFragment bindingDetailFragment = new BindingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        bindingDetailFragment.setArguments(args);
        return bindingDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getString(ID_KEY, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ModuleMainFragmentBindingDetailBinding bindingDetailBinding = ModuleMainFragmentBindingDetailBinding.inflate(inflater, container, false);
        bindingDetailBinding.setView(this);
        bindingDetailBinding.setViewmodel(mDetailViewModel);
        return bindingDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDetailViewModel.start(mId);
    }
}
