package com.example.administrator.lsn_11_demo;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("jett","MyApplication onCreate()");
    }
}
