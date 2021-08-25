package com.maniu.notchscreenmaniu;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

public class SystemAttrbute {

    //    小米  AMS
    public static  boolean getXiaomi(Context context) {
        boolean ret = false;
        ClassLoader cl = context.getClassLoader();
        Class SystemProperties = null;
        try {
            SystemProperties = cl.loadClass("android.os.SystemProperties");
            Method get = SystemProperties.getMethod("getInt", String.class, int.class);
            ret= (Integer)get.invoke(null, "ro.miui.notch", 0)==1;
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    public static boolean hasOppoNotch(Context context) {
            boolean ret=context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
            Log.i("tuch", "hasOppoNotch: " + ret);
            return ret;


    }
//FtFeature  ----> 增加了FtFeature
    public static boolean hasVivoNotch(Context context) {
        ClassLoader classLoader = context.getClassLoader();
        int VIVO_NOTCH = 0x00000020; // 是否有刘海
        boolean ret = false;
        try {
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");

            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret= (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

}
