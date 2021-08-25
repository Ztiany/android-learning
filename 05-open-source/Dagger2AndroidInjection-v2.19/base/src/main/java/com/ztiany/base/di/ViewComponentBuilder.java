package com.ztiany.base.di;

import android.view.View;

import dagger.BindsInstance;

/**
 * Created by froger_mcs on 14/09/16.
 */
public interface ViewComponentBuilder<T extends View, C extends ViewComponent<T>> {

    @BindsInstance
    ViewComponentBuilder bindInstance(T t);

    C build();

}