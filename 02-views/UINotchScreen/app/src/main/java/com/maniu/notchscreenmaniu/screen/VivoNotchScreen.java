package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.view.Window;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

import java.lang.reflect.Method;

public class VivoNotchScreen implements INotchSupport {
    @Override
    public boolean isNotchScreen(Window window) {
        ClassLoader classLoader = window.getContext().getClassLoader();
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
//vivo+oppo  差不多的
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }
        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow() );
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
