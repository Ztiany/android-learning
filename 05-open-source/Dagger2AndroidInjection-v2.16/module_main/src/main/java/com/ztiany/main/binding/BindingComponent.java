package com.ztiany.main.binding;

import com.ztiany.base.di.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 15:16
 */
@Subcomponent(
        modules = {
                BindingModule.class
        })
@ActivityScope
public interface BindingComponent extends AndroidInjector<BindingActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BindingActivity> {

    }

}
