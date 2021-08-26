package com.ztiany.base.di;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import dagger.android.AndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-06-21 14:55
 */
public class ViewInjection {

    private static final String TAG = ViewInjection.class.getSimpleName();

    public static void inject(View view) {
        if (null == view) {
            throw new NullPointerException();
        }
        HasViewInjector hasViewInjector = findHasFragmentInjector(view);
        Log.d(TAG, String.format(
                "An injector for %s was found in %s",
                view.getClass().getCanonicalName(),
                hasViewInjector.getClass().getCanonicalName()));

        AndroidInjector<View> viewAndroidInjector = hasViewInjector.viewInjector();
        if (viewAndroidInjector == null) {
            throw new NullPointerException("viewAndroidInjector return null");
        }
        viewAndroidInjector.inject(view);
    }

    private static HasViewInjector findHasFragmentInjector(View view) {
        Context context = view.getContext();
        if (context instanceof HasViewInjector) {
            return (HasViewInjector) context;
        }
        Application application = (Application) context.getApplicationContext();
        if (application instanceof HasViewInjector) {
            return (HasViewInjector) application;
        }
        throw new IllegalArgumentException(
                String.format("No injector was found for %s", view.getClass().getCanonicalName()));
    }
}
