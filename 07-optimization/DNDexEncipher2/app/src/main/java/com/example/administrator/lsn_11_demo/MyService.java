package com.example.administrator.lsn_11_demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;



public class MyService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("jett", "service:" + getApplication());
        Log.i("jett", "service:" + getApplicationContext());
        Log.i("jett", "service:" + getApplicationInfo().className);
    }
}
