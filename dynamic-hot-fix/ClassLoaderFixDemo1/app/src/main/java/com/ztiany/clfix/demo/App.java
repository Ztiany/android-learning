package com.ztiany.clfix.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * 参考：https://www.jianshu.com/p/5f390be47ce8
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-17 23:27
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        FixDexUtils.loadFixedDex(base);
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
