package com.dn.lsn16_demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.dn.lsn16_demo.utils.FixDexUtils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        FixDexUtils.loadFixedDex(this);
    }
}
