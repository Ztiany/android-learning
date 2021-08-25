package com.ztiany.view.drawable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-12-10 23:38
 */
public class FishDrawableFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawable_fragment_fish, container, false);
    }

}
