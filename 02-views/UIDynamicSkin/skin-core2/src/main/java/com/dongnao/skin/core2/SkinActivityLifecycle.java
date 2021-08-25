package com.dongnao.skin.core2;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;

import com.dongnao.skin.core2.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "SkinActivityLifecycle";

    HashMap<Activity, SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        Log.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");

        /* 更新状态栏 */
        SkinThemeUtils.updateStatusBar(activity);

        /* 字体 */
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        //获得Activity的布局加载器
        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity, typeface);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory);
        //注册观察者
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mLayoutFactoryMap.put(activity, skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //删除观察者
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }

    public void updateSkin(Activity activity) {
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.get(activity);
        skinLayoutFactory.update(null, null);
    }

}
