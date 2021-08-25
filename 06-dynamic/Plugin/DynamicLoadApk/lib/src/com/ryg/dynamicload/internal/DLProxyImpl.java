/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇,Mr.Simple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryg.dynamicload.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ryg.dynamicload.DLPlugin;
import com.ryg.utils.DLConfigs;
import com.ryg.utils.DLConstants;

import java.lang.reflect.Constructor;

/**
 * This is a plugin activity proxy, the proxy will create the plugin activity
 * with reflect, and then call the plugin activity's attach、onCreate method, at
 * this time, the plugin activity is running.
 *
 * @author mrsimple
 */
public class DLProxyImpl {

    private static final String TAG = "DLProxyImpl";

    private String mClass;
    private String mPackageName;

    private DLPluginPackage mPluginPackage;
    private DLPluginManager mPluginManager;

    private AssetManager mAssetManager;
    private Resources mResources;
    private Theme mTheme;

    private ActivityInfo mActivityInfo;
    private Activity mProxyActivity;
    protected DLPlugin mPluginActivity;
    public ClassLoader mPluginClassLoader;

    public DLProxyImpl(Activity activity) {
        mProxyActivity = activity;
    }


    /*1 当代理Activity被启动，调用此方法来初始化*/
    public void onCreate(Intent intent) {

        // set the extra's class loader
        //把Intent的ExtrasClassLoader替换成插件中的ClassLoader，这个ClassLoader
        //用于反序列化Intent中的可序列化对象
        intent.setExtrasClassLoader(DLConfigs.sPluginClassloader);

        //获取插件包名
        mPackageName = intent.getStringExtra(DLConstants.EXTRA_PACKAGE);
        //获取插件Activity的类名
        mClass = intent.getStringExtra(DLConstants.EXTRA_CLASS);
        Log.d(TAG, "mClass=" + mClass + " mPackageName=" + mPackageName);

        mPluginManager = DLPluginManager.getInstance(mProxyActivity);
        mPluginPackage = mPluginManager.getPackage(mPackageName);
        mAssetManager = mPluginPackage.assetManager;
        mResources = mPluginPackage.resources;

        initializeActivityInfo();
        handleActivityInfo();
        launchTargetActivity();
    }

    /*初始化Activity信息*/
    private void initializeActivityInfo() {
        PackageInfo packageInfo = mPluginPackage.packageInfo;
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            if (mClass == null) {
                mClass = packageInfo.activities[0].name;
            }

            //Finals 修复主题BUG
            int defaultTheme = packageInfo.applicationInfo.theme;

            for (ActivityInfo a : packageInfo.activities) {

                if (a.name.equals(mClass)) {
                    mActivityInfo = a;
                    // Finals ADD 修复主题没有配置的时候插件异常
                    if (mActivityInfo.theme == 0) {
                        if (defaultTheme != 0) {//如果插件中有theme就替换
                            mActivityInfo.theme = defaultTheme;
                        } else {//否则使用默认的
                            if (Build.VERSION.SDK_INT >= 14) {
                                mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                            } else {
                                mActivityInfo.theme = android.R.style.Theme;
                            }
                        }
                    }

                }
            }

        }
    }

    private void handleActivityInfo() {
        Log.d(TAG, "handleActivityInfo, theme=" + mActivityInfo.theme);
        if (mActivityInfo.theme > 0) {
            mProxyActivity.setTheme(mActivityInfo.theme);
        }
        Theme superTheme = mProxyActivity.getTheme();
        mTheme = mResources.newTheme();
        //复制Theme
        mTheme.setTo(superTheme);
        // Finals适配三星以及部分加载XML出现异常BUG
        try {
            //将新的属性值放置到主题中
            mTheme.applyStyle(mActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: handle mActivityInfo.launchMode here in the future.
    }

    /*启动目标插件Activity*/
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void launchTargetActivity() {
        try {
            Class<?> localClass = getClassLoader().loadClass(mClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[]{});
            Object instance = localConstructor.newInstance(new Object[]{});
            mPluginActivity = (DLPlugin) instance;
            //调用代理的attach方法
            ((DLAttachable) mProxyActivity).attach(mPluginActivity, mPluginManager);
            Log.d(TAG, "instance = " + instance);
            //调用创建的attach方法
            // attach the proxy activity and plugin package to the mPluginActivity
            mPluginActivity.attach(mProxyActivity, mPluginPackage);
            Bundle bundle = new Bundle();
            bundle.putInt(DLConstants.FROM, DLConstants.FROM_EXTERNAL);
            //调用插件的onCreate方法
            mPluginActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClassLoader getClassLoader() {
        return mPluginPackage.classLoader;
    }

    public AssetManager getAssets() {
        return mAssetManager;
    }

    public Resources getResources() {
        return mResources;
    }

    public Theme getTheme() {
        return mTheme;
    }

    public DLPlugin getRemoteActivity() {
        return mPluginActivity;
    }
}
