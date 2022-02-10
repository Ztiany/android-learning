package com.ztiany.recyclerview.viewpager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class PagerFragment extends Fragment {

    private static final String TAG = "PagerFragment";

    public static Fragment newInstance(String name) {
        PagerFragment pagerFragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString("KEY", name);
        pagerFragment.setArguments(args);
        return pagerFragment;
    }

    private int mBackground;
    private String mName = "Default";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Random mRandom = new Random();
        mBackground = Color.argb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));

        Bundle arguments = getArguments();
        if (arguments != null) {
            mName = arguments.getString("KEY", "");
        }
        Log.d(TAG, "onCreate() " + mName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppCompatTextView textView = new AppCompatTextView(requireContext());
        textView.setText(mName);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setBackgroundColor(mBackground);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() " + mName);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() " + mName);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() " + mName);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() " + mName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() " + mName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() " + mName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() " + mName);
    }

}
