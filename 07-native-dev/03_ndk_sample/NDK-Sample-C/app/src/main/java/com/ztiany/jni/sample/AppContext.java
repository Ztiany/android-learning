package com.ztiany.jni.sample;

import android.app.Application;
import android.widget.Toast;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-11-23 16:09
 */
public class AppContext extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static void showToast(String message) {
        Toast.makeText(sApplication, message, Toast.LENGTH_SHORT).show();
    }

}
