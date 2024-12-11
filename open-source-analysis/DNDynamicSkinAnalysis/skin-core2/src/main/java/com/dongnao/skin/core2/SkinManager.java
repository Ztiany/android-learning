package com.dongnao.skin.core2;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.dongnao.skin.core2.utils.SkinPreference;
import com.dongnao.skin.core2.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by Administrator on 2018/3/16 0016.
 */
public class SkinManager extends Observable {

    private static SkinManager instance;
    private SkinActivityLifecycle skinActivityLifecycle;
    private Application application;

    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (null == instance) {
                instance = new SkinManager(application);
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResources.init(application);
        //注册Activity生命周期回调
        skinActivityLifecycle = new SkinActivityLifecycle();
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle);
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    /**
     * 加载皮肤包 并 更新
     *
     * @param path 皮肤包路径
     */
    public void loadSkin(String path) {
        //还原默认皮肤包
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                // 添加资源进入资源管理器
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, path);

                Resources resources = application.getResources();
                // 横竖、语言
                Resources skinResource = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
                //获取外部Apk(皮肤包) 包名
                PackageManager mPm = application.getPackageManager();
                PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);
                //保存当前使用的皮肤包
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //应用皮肤包
        setChanged();
        //通知观察者
        notifyObservers();
    }

    public void updateSkin(Activity activity) {
        skinActivityLifecycle.updateSkin( activity);
    }

}
