package com.ztiany.base;

import android.app.Activity;
import android.view.View;

import com.ztiany.base.di.HasViewInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class HomeAppContext extends AppContext implements HasActivityInjector, HasViewInjector<View> {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject
    DispatchingAndroidInjector<View> mViewDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<View> viewInjector() {
        return mViewDispatchingAndroidInjector;
    }
}