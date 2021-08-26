package com.ztiany.module_a;

import android.app.Activity;

import com.ztiany.module_a.feature.ModuleAComponent;
import com.ztiany.module_a.feature.ModuleAActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {ModuleAComponent.class})
public abstract class ModuleABuildersModule {

    @Binds
    @IntoMap
    @ActivityKey(ModuleAActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity> bindActivityFactory(ModuleAComponent.Builder builder);

}