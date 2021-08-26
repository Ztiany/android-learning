package com.ztiany.base;

import android.view.View;

import com.ztiany.base.di.HasViewInjector;
import com.ztiany.base.di.ViewComponentBuilder;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class HomeAppContext extends AppContext implements HasAndroidInjector, HasViewInjector {

    @Inject
    DispatchingAndroidInjector<Object> dispatchingActivityInjector;

    @Inject
    Map<Class<? extends View>, Provider<ViewComponentBuilder>> viewBuilders;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public Map<Class<? extends View>, Provider<ViewComponentBuilder>> viewInjectors() {
        return viewBuilders;
    }

}