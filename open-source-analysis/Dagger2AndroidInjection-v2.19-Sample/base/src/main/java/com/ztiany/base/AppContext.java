package com.ztiany.base;

import android.app.Application;
import android.content.Context;

import dagger.android.HasActivityInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 11:02
 */
public abstract class AppContext extends Application implements HasActivityInjector{

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.register(this);
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }

}
