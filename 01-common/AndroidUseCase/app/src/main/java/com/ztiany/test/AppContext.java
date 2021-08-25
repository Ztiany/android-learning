package com.ztiany.test;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.github.gzuliyujiang.oaid.DeviceID;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-16 16:37
 */
public class AppContext extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}