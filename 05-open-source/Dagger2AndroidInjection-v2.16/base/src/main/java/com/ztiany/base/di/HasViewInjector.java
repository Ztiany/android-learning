package com.ztiany.base.di;

import android.app.Activity;
import android.view.View;

import dagger.android.AndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-06-21 14:58
 */
public interface HasViewInjector<V> {

    /**
     * Returns an {@link AndroidInjector} of {@link Activity}s.
     */
    AndroidInjector<View> viewInjector();
}
