package com.ztiany.androidipc;

import android.app.Application;
import android.util.Log;

public class IPCContext extends Application {

    private static final String TAG = IPCContext.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "IPCContext created" + this);
    }

}
