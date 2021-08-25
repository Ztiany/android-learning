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

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;
import com.ryg.dynamicload.DLBasePluginService;
import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.DLProxyFragmentActivity;
import com.ryg.dynamicload.DLProxyService;
import com.ryg.utils.DLConstants;
import com.ryg.utils.SoLibManager;

import dalvik.system.DexClassLoader;

public class DLPluginManager {

    private static final String TAG = "DLPluginManager";

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} start
     * success
     */
    public static final int START_RESULT_SUCCESS = 0;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} package
     * not found
     */
    public static final int START_RESULT_NO_PKG = 1;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class
     * not found
     */
    public static final int START_RESULT_NO_CLASS = 2;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class
     * type error
     */
    public static final int START_RESULT_TYPE_ERROR = 3;

    private static DLPluginManager sInstance;
    private Context mContext;

    //用于保存所有的插件包
    private final HashMap<String, DLPluginPackage> mPackagesHolder = new HashMap<String, DLPluginPackage>();

    private int mFrom = DLConstants.FROM_INTERNAL;

    //用于存储插件so的路径
    private String mNativeLibDir = null;

    private int mResult;

    private DLPluginManager(Context context) {
        mContext = context.getApplicationContext();
        mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static DLPluginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DLPluginManager.class) {
                if (sInstance == null) {
                    sInstance = new DLPluginManager(context);
                }
            }
        }

        return sInstance;
    }

    /**
     * Load a apk. Before start a plugin Activity, we should do this first.<br/>
     * NOTE : will only be called by host apk.
     * 由宿主Apk来加载插件apk
     *
     * @param dexPath 目标apk的路径
     */
    public DLPluginPackage loadApk(String dexPath) {
        // when loadApk is called by host apk, we assume that plugin is invoked by host.
        //当插件Apk由主机Apk调用时，我们假设插件是由主机调用的。
        return loadApk(dexPath, true);
    }

    /**
     * @param dexPath  plugin path
     * @param hasSoLib whether exist so lib in plugin
     * @return DLPluginPackage
     */
    public DLPluginPackage loadApk(final String dexPath, boolean hasSoLib) {
        //从内部加载？
        mFrom = DLConstants.FROM_EXTERNAL;

        /*从给定的路径中获取APK信息*/
        PackageInfo packageInfo = mContext.getPackageManager()
                .getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);

        if (packageInfo == null) {
            return null;
        }

        //生成插件包对象DLPluginPackage
        DLPluginPackage pluginPackage = preparePluginEnv(packageInfo, dexPath);

        //如果有so库则需要复制so文件
        if (hasSoLib) {
            copySoLib(dexPath);
        }

        return pluginPackage;
    }

    /**
     * copy .so file to pluginlib dir. 复制
     *
     * @param dexPath
     */
    private void copySoLib(String dexPath) {
        // 目前实在主线程进行io操作的
        SoLibManager.getSoLoader().copyPluginSoLib(mContext, dexPath, mNativeLibDir);
    }

    public DLPluginPackage getPackage(String packageName) {
        return mPackagesHolder.get(packageName);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 实际解析APK的过程
    ///////////////////////////////////////////////////////////////////////////

    /**
     * prepare plugin runtime env, has DexClassLoader, Resources, and so on.
     *
     * @param packageInfo
     * @param dexPath
     * @return
     */
    private DLPluginPackage preparePluginEnv(PackageInfo packageInfo, String dexPath) {
        //如果有缓存，则获取缓存的DLPluginPackage
        DLPluginPackage pluginPackage = mPackagesHolder.get(packageInfo.packageName);
        if (pluginPackage != null) {
            return pluginPackage;
        }
        /*
        否则去解析apk，三个重点
            1. 设置插件的ClassLoader
            2. 设置插件的AssetManager
            3. 设置插件的Resources
        */
        DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
        AssetManager assetManager = createAssetManager(dexPath);
        Resources resources = createResources(assetManager);
        // create pluginPackage
        pluginPackage = new DLPluginPackage(dexClassLoader, resources, packageInfo);
        mPackagesHolder.put(packageInfo.packageName, pluginPackage);
        return pluginPackage;
    }

    private String dexOutputPath;

    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        dexOutputPath = dexOutputDir.getAbsolutePath();
        //dexPath就是插件apk的存储路径
        //dexOutputPath 表示优化过的dexPath
        //插件so路径
        //父加载器使用宿主的ClassLoader
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, mNativeLibDir, mContext.getClassLoader());
        return loader;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            //把插件apk的路径添加到AssetManager中
            //为什么可以这样操作？
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //创建插件的Resource
    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保留常用的功能API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * {@link #startPluginActivityForResult(Activity, DLIntent, int)}
     */
    public int startPluginActivity(Context context, DLIntent dlIntent) {
        return startPluginActivityForResult(context, dlIntent, -1);
    }

    /**
     * @param context     上下文
     * @param dlIntent    dl意图
     * @param requestCode 请求码
     * @return One of below: {@link #START_RESULT_SUCCESS}
     * {@link #START_RESULT_NO_PKG} {@link #START_RESULT_NO_CLASS}
     * {@link #START_RESULT_TYPE_ERROR}
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public int startPluginActivityForResult(Context context, DLIntent dlIntent, int requestCode) {

        //当调用loadApk后mFrom的值为FROM_EXTERNAL
        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            performStartActivityForResult(context, dlIntent, requestCode);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        String packageName = dlIntent.getPluginPackage();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("disallow null packageName.");
        }

        DLPluginPackage pluginPackage = mPackagesHolder.get(packageName);
        if (pluginPackage == null) {
            return START_RESULT_NO_PKG;
        }

        //获取Activity的完整类路径
        final String className = getPluginActivityFullPath(dlIntent, pluginPackage);
        //加载插件中的class，这里用的是对应插件的classLoader
        Class<?> clazz = loadPluginClass(pluginPackage.classLoader, className);
        if (clazz == null) {
            return START_RESULT_NO_CLASS;
        }

        // get the proxy activity class, the proxy activity will launch the
        // plugin activity.
        Class<? extends Activity> activityClass = getProxyActivityClass(clazz);
        if (activityClass == null) {
            return START_RESULT_TYPE_ERROR;
        }

        // put extra data
        dlIntent.putExtra(DLConstants.EXTRA_CLASS, className);//插件类名
        dlIntent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);//插件所在包名
        dlIntent.setClass(mContext, activityClass);//真正启动的Activity是代理Activity
        performStartActivityForResult(context, dlIntent, requestCode);
        return START_RESULT_SUCCESS;
    }

    private String getPluginActivityFullPath(DLIntent dlIntent, DLPluginPackage pluginPackage) {
        String className = dlIntent.getPluginClass();
        //如果className为null则启动则返回默认的Activity名
        className = (className == null ? pluginPackage.defaultActivity : className);
        if (className.startsWith(".")) {
            className = dlIntent.getPluginPackage() + className;
        }
        return className;
    }

    /**
     * 根据目标Activity的类型返回对应的代理Activity的类型，比如：
     * Activity对应Activity
     * FragmentActivity对应FragmentActivity
     * get the proxy activity class, the proxy activity will delegate the plugin activity
     *
     * @param clazz target activity's class
     * @return Activity class
     */
    private Class<? extends Activity> getProxyActivityClass(Class<?> clazz) {
        Class<? extends Activity> activityClass = null;
        if (DLBasePluginActivity.class.isAssignableFrom(clazz)) {
            activityClass = DLProxyActivity.class;
        } else if (DLBasePluginFragmentActivity.class.isAssignableFrom(clazz)) {
            activityClass = DLProxyFragmentActivity.class;
        }
        return activityClass;
    }

    /**
     * 启动代理Activity
     */
    private void performStartActivityForResult(Context context, DLIntent dlIntent, int requestCode) {
        Log.d(TAG, "launch " + dlIntent.getPluginClass());
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(dlIntent, requestCode);
        } else {
            context.startActivity(dlIntent);
        }
    }

    //-----------启动 Service----------
    public int startPluginService(final Context context, final DLIntent dlIntent) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            context.startService(dlIntent);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        fetchProxyServiceClass(dlIntent, new OnFetchProxyServiceClass() {
            @Override
            public void onFetch(int result, Class<? extends Service> proxyServiceClass) {
                if (result == START_RESULT_SUCCESS) {
                    dlIntent.setClass(context, proxyServiceClass);
                    // start代理Service
                    context.startService(dlIntent);
                }
                mResult = result;
            }
        });

        return mResult;
    }

    public int stopPluginService(final Context context, final DLIntent dlIntent) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            context.stopService(dlIntent);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        fetchProxyServiceClass(dlIntent, new OnFetchProxyServiceClass() {
            @Override
            public void onFetch(int result, Class<? extends Service> proxyServiceClass) {
                if (result == START_RESULT_SUCCESS) {
                    dlIntent.setClass(context, proxyServiceClass);
                    // stop代理Service
                    context.stopService(dlIntent);
                }
                mResult = result;
            }
        });

        return mResult;
    }

    public int bindPluginService(final Context context, final DLIntent dlIntent, final ServiceConnection conn,
                                 final int flags) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            context.bindService(dlIntent, conn, flags);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        fetchProxyServiceClass(dlIntent, new OnFetchProxyServiceClass() {
            @Override
            public void onFetch(int result, Class<? extends Service> proxyServiceClass) {
                if (result == START_RESULT_SUCCESS) {
                    dlIntent.setClass(context, proxyServiceClass);
                    // Bind代理Service
                    context.bindService(dlIntent, conn, flags);
                }
                mResult = result;
            }
        });

        return mResult;
    }

    public int unBindPluginService(final Context context, DLIntent dlIntent, final ServiceConnection conn) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            context.unbindService(conn);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        fetchProxyServiceClass(dlIntent, new OnFetchProxyServiceClass() {
            @Override
            public void onFetch(int result, Class<? extends Service> proxyServiceClass) {
                if (result == START_RESULT_SUCCESS) {
                    // unBind代理Service
                    context.unbindService(conn);
                }
                mResult = result;
            }
        });
        return mResult;

    }

    /**
     * 获取代理ServiceClass
     *
     * @param dlIntent
     * @param fetchProxyServiceClass
     */
    private void fetchProxyServiceClass(DLIntent dlIntent, OnFetchProxyServiceClass fetchProxyServiceClass) {
        String packageName = dlIntent.getPluginPackage();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("disallow null packageName.");
        }
        DLPluginPackage pluginPackage = mPackagesHolder.get(packageName);
        if (pluginPackage == null) {
            fetchProxyServiceClass.onFetch(START_RESULT_NO_PKG, null);
            return;
        }

        // 获取要启动的Service的全名
        String className = dlIntent.getPluginClass();
        Class<?> clazz = loadPluginClass(pluginPackage.classLoader, className);
        if (clazz == null) {
            fetchProxyServiceClass.onFetch(START_RESULT_NO_CLASS, null);
            return;
        }

        Class<? extends Service> proxyServiceClass = getProxyServiceClass(clazz);
        if (proxyServiceClass == null) {
            fetchProxyServiceClass.onFetch(START_RESULT_TYPE_ERROR, null);
            return;
        }

        // put extra data
        dlIntent.putExtra(DLConstants.EXTRA_CLASS, className);
        dlIntent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);
        fetchProxyServiceClass.onFetch(START_RESULT_SUCCESS, proxyServiceClass);
    }

    // zhangjie1980 重命名 loadPluginActivityClass -> loadPluginClass
    private Class<?> loadPluginClass(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private Class<? extends Service> getProxyServiceClass(Class<?> clazz) {
        Class<? extends Service> proxyServiceClass = null;
        if (DLBasePluginService.class.isAssignableFrom(clazz)) {
            proxyServiceClass = DLProxyService.class;
        }
        // 后续可能还有IntentService，待补充
        return proxyServiceClass;
    }

    private interface OnFetchProxyServiceClass {
        public void onFetch(int result, Class<? extends Service> proxyServiceClass);
    }

}
