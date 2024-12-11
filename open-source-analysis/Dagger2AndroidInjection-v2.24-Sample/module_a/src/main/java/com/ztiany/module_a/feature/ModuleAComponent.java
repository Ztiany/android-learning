package com.ztiany.module_a.feature;

import com.ztiany.base.di.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:23
 */
@Subcomponent(modules = ModuleAModule.class)
@ActivityScope
public interface ModuleAComponent extends AndroidInjector<ModuleAActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ModuleAActivity> {

    }

}
