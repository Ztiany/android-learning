package com.maniu.notchscreenmaniu.screen;

import android.os.Build;
import android.text.TextUtils;

public class DeviceTools {
    public static final boolean isMiui() {
      String manufacturer=SystemProperties.getInstance().get("ro.miui.version.name") ;
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }
    public static final boolean isVivo() {
        String manufacturer = SystemProperties.getInstance().get("ro.vivo.os.name");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }
//    oppo   华为   大部分

    public static boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)){
            if (manufacturer.contains("HUAWEI")) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isOppo() {
        String manufacturer = Build.MANUFACTURER;
        if("oppo".equalsIgnoreCase(manufacturer)){
            return true;
        }
        return false;
    }

    public static final boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if("samsung".equalsIgnoreCase(manufacturer)){
            return true;
        }
        return false;
    }
}
