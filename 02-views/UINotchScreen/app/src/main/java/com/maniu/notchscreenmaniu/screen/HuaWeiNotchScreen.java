package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HuaWeiNotchScreen implements INotchSupport {
    @Override
    public boolean isNotchScreen(Window window) {
//        刘海品
        boolean isNotchScreen = false;
        try {
            ClassLoader cl = window.getContext().getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            isNotchScreen = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            return false;
        }
        return isNotchScreen;
    }
//oppo  + vivo      华为 +小米
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }

        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = window.getContext().getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        } finally {
            return ret[1];
        }
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        /**
         * 刘海屏全屏显示FLAG
         */
       int FLAG_NOTCH_SUPPORT=0x00010000;
        Window window = activity.getWindow();
        if (isNotchScreen(activity.getWindow())) {

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            try {
                Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
                Constructor con=layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
                Object layoutParamsExObj=con.newInstance(layoutParams);
                Method method=layoutParamsExCls.getMethod("addHwFlags", int.class);
                method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |InstantiationException
                    | InvocationTargetException e) {
                Log.e("test", "hw add notch screen flag api error");
            } catch (Exception e) {
                Log.e("test", "other Exception");
            }
        }
        onBindCallBackWithNotchProperty(activity, notchCallBack);
    }
    protected void onBindCallBackWithNotchProperty(Activity activity,

                                                   OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setMarginTop(getNotchHeight(activity.getWindow()));
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }
    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        this.fullScreenUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.showFakeNotchView(activity.getWindow(), getNotchHeight(activity.getWindow()));
        }
    }
}
