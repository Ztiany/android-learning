package com.ztiany.classloader;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-01-04 19:22
 */
public class AppContext extends Application {

    private static final String TAG = AppContext.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //hookPluginClassLoader();
        printClassLoader();
        //loadPluginClass();
    }

    private void loadPluginClass() {
        try {
            Class<?> aClass = Class.forName("com.google.gson.Gson");
            if (aClass != null) {
                Log.d(TAG, "aClass:" + aClass);
                Log.d(TAG, "aClass.getClassLoader():" + aClass.getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printClassLoader() {
        Log.d(TAG, "ClassLoader.getSystemClassLoader(): " + ClassLoader.getSystemClassLoader());

        Log.d(TAG, "getClassLoader(): " + getClassLoader());

        Log.d(TAG, "MainActivity.class.getClassLoader(): " + MainActivity.class.getClassLoader());

        Log.d(TAG, "String.class.getClassLoader(): " + String.class.getClassLoader());

        ClassLoader classLoader = MainActivity.class.getClassLoader();
        while (classLoader != null) {
            Log.d(TAG, "" + classLoader);
            classLoader = classLoader.getParent();
        }
    }


    private void hookPluginClassLoader() {
        File dexDir = new File(getFilesDir(), "plugins");
        dexDir.mkdir();
        File dexFile = new File(dexDir, "gson.dex");

        File dexOpt = getCacheDir();

        //把Assets下的libs.apk写到dlibs目录下
        try {
            InputStream ins = getAssets().open("gson.dex");
            if (dexFile.length() != ins.available()) {
                FileOutputStream fos = new FileOutputStream(dexFile);
                byte[] buf = new byte[4096];
                int l;
                while ((l = ins.read(buf)) != -1) {
                    fos.write(buf, 0, l);
                }
                fos.close();
            }
            ins.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //获取ClassLoader
        ClassLoader cl = getClassLoader();
        //够建一个新的DexClassLoader
        DexClassLoader dcl = new PluginDexClassLoader(dexFile.getAbsolutePath(), dexOpt.getAbsolutePath(), null, cl.getParent());
        //把DexClassLoader插入到类加载器委托机制的中间，这样就可以加载外部dex中的类了
        try {
            Field f = ClassLoader.class.getDeclaredField("parent");
            f.setAccessible(true);
            f.set(cl, dcl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public class PluginDexClassLoader extends DexClassLoader {

        public PluginDexClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
            super(dexPath, optimizedDirectory, librarySearchPath, parent);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Log.d(TAG, "findClass() called with: name = [" + name + "]");
            return super.findClass(name);
        }
    }
}
