package com.ztiany.test.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ztiany.test.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class FragmentsActivity extends AppCompatActivity {

    private static final int ID = R.id.activity_content;

    private static final String TAG = FragmentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged() called");
            }
        });

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                Log.d(TAG, "onFragmentResumed:" + f);
            }
        }, true);

        FragmentUtils.init(ID);
        setContentView(R.layout.activity_fragments);
    }

    public void add1(View view) {
        FragmentUtils.replace(getSupportFragmentManager(), new TestFragment1());
    }

    public void add2(View view) {
        FragmentUtils.replace(getSupportFragmentManager(), new TestFragment2(), true, true);
    }

    public void add3(View view) {
        FragmentUtils.replace(getSupportFragmentManager(), new TestFragment3(), true, true);
    }

    public void pop(View view) {
//        getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        getSupportFragmentManager().popBackStack(TestFragment2.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

}
