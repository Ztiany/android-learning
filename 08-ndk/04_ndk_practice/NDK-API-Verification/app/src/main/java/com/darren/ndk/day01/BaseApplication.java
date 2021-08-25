package com.darren.ndk.day01;

import android.app.Application;

/**
 * Created by hcDarren on 2018/1/20.
 */

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SignatureUtils.signatureVerify(this);
    }

}
