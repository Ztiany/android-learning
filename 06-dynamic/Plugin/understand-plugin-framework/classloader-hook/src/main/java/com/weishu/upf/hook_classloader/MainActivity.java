package com.weishu.upf.hook_classloader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.weishu.upf.hook_classloader.ams_hook.AMSHookHelper;
import com.weishu.upf.hook_classloader.classloder_hook.BaseDexClassLoaderHookHelper;
import com.weishu.upf.hook_classloader.classloder_hook.LoadedApkClassLoaderHookHelper;

import java.io.File;

/**
 * @author weishu
 * @date 16/3/28
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int PATCH_BASE_CLASS_LOADER = 1;
    private static final int CUSTOM_CLASS_LOADER = 2;

    private static final int HOOK_METHOD = PATCH_BASE_CLASS_LOADER;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button t = new Button(this);
        t.setText("test button");
        setContentView(t);

        Log.d(TAG, "context classloader: " + getApplicationContext().getClassLoader());

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent t = new Intent();
                    t.setComponent(new ComponentName("com.ztiany.noresourceapp", "com.ztiany.noresourceapp.MainActivity"));
                    startActivity(t);

                    //发个广播给插件
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendBroadcast(new Intent("com.ztiany.noresource.test"));
                        }
                    }, 2000);

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    @SuppressWarnings("all")
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            Utils.extractAssets(newBase, "no_resource.apk");

            if (HOOK_METHOD == PATCH_BASE_CLASS_LOADER) {
                File dexFile = getFileStreamPath("no_resource.apk.apk");
                File optDexFile = getFileStreamPath("no_resource.apkdex");
                BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), dexFile, optDexFile);
            } else {
                File fileStreamPath = getFileStreamPath("no_resource.apk.apk");
                LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(fileStreamPath);
            }

            AMSHookHelper.hookActivityManagerNative();
            AMSHookHelper.hookActivityThreadHandler();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
