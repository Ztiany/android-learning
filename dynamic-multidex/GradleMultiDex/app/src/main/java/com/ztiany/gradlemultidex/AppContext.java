package com.ztiany.gradlemultidex;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-13 21:53
 */
public class AppContext extends Application {

    private static final String TAG = AppContext.class.getSimpleName();
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        /*
        ClassLoader.getSystemClassLoader():
        dalvik.system.PathClassLoader[
                DexPathList[[directory "."],
                nativeLibraryDirectories=[/vendor/lib64, /system/lib64]]]

        getClassLoader():dalvik.system.PathClassLoader[
                DexPathList[[zip file "/data/app/com.ztiany.gradlemultidex-2/base.apk"],
                nativeLibraryDirectories=[/data/app/com.ztiany.gradlemultidex-2/lib/arm64, /vendor/lib64, /system/lib64]]]
         */

        Log.d(TAG, "ClassLoader.getSystemClassLoader():" + ClassLoader.getSystemClassLoader());
        Log.d(TAG, "com/ztiany/gradlemultidex/AppContext.class():" + getClassLoader());
        Log.d(TAG, "MainActivity.class.getClassLoader():" + MainActivity.class.getClassLoader());

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Log.d(TAG, packageInfo.applicationInfo.sourceDir);
            Log.d(TAG, packageInfo.applicationInfo.dataDir);
            Log.d(TAG, packageInfo.applicationInfo.nativeLibraryDir);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
