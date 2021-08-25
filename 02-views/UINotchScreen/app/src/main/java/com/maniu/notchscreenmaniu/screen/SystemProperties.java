package com.maniu.notchscreenmaniu.screen;

import android.util.Log;

import java.lang.reflect.Method;

public class SystemProperties {

    private static Method getStringProperty;
    private static  SystemProperties sSystemProperties;

    public static SystemProperties getInstance() {
        if (sSystemProperties ==null){
            synchronized (SystemProperties.class){
                if (sSystemProperties == null) {
                    sSystemProperties = new SystemProperties();
                }
            }
        }
        return sSystemProperties;
    }
    private SystemProperties(){
        getStringProperty = getMethod(getClass("android.os.SystemProperties"));
    }


    private Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }
    private Method getMethod(Class clz) {
        Method method;
        try {
            method = clz != null ? clz.getMethod("get", String.class) : null;
        } catch (Exception e) {
            Log.e("tuch", e.getMessage());
            method = null;
        }
        return method;
    }
    public final String get(String key) {
        String value;
        try {
            value = (String)   getStringProperty.invoke( null, key)   ;
            if (value != null) {
                return value.trim();
            } else {
                return "";
            }
        } catch (Exception var4) {
            return "";
        }
    }

}
