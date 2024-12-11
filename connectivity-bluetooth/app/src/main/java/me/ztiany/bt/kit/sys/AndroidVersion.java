package me.ztiany.bt.kit.sys;

import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

public class AndroidVersion {

    private AndroidVersion() {
        throw new UnsupportedOperationException();
    }

    public static boolean above(int sdkVersion) {
        return Build.VERSION.SDK_INT > sdkVersion;
    }

    public static boolean before(int sdkVersion) {
        return Build.VERSION.SDK_INT < sdkVersion;
    }

    @ChecksSdkIntAtLeast(parameter = 0)
    public static boolean atLeast(int sdkVersion) {
        return Build.VERSION.SDK_INT >= sdkVersion;
    }

    public static boolean at(int sdkVersion) {
        return Build.VERSION.SDK_INT == sdkVersion;
    }

    public static boolean in(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }

}