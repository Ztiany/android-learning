package com.ztiany.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


/**
 * <pre>
 *          工具类基础
 * </pre>
 *
 * @author Ztiany
 *         date :   2016-03-19 23:09
 *         email:    ztiany3@gmail.com
 */
public class BaseUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mApplication;

    public static void init(Context application) {
        if (mApplication == null) {
            mApplication = application;
        }
    }

    public static Context getAppContext() {
        return mApplication;
    }

    public static Resources getResources() {
        return BaseUtils.getAppContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return BaseUtils.getAppContext().getTheme();
    }

    public static AssetManager getAssets() {
        return BaseUtils.getAppContext().getAssets();
    }

    public static Configuration getConfiguration() {
        return BaseUtils.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return BaseUtils.getResources().getDisplayMetrics();
    }

}
