package com.ztiany.main.binding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ztiany.main.R;
import com.ztiany.main.binding.presentation.BindingDetailFragment;
import com.ztiany.main.binding.presentation.BindingListFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:35
 */
public class BindingActivity extends AppCompatActivity implements HasSupportFragmentInjector, BindingListFragment.BindingListFragmentCallback {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_main_activity_binding);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.module_main_fl_content, BindingListFragment.newInstance(), BindingListFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void showDetail(String id) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(BindingDetailFragment.class.getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.module_main_fl_content, BindingDetailFragment.newInstance(id), BindingListFragment.class.getName())
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
