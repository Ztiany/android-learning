package com.enjoy.qzonefix;

import android.app.Application;
import android.content.Context;

import com.enjoy.patch.EnjoyFix;

import java.io.File;

/**
 * @author Lance
 * @date 2019-09-03
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //加载补丁
        EnjoyFix.installPatch(this, new File("/sdcard/Download/patch.jar"));
    }

}
