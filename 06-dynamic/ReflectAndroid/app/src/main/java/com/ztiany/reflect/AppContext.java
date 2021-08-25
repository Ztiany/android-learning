package com.ztiany.reflect;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-12-28 23:31
 */
public class AppContext extends Application {

    private static final String TAG = AppContext.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ActivityThreadHook.getInstance().hookActivityThread();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }
}
