package com.ztiany.main.main;

import com.ztiany.base.di.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = MainModule.class)
@ActivityScope
public interface MainActivityComponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {

    }

}