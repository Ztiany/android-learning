package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import androidx.annotation.RequiresApi;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

import java.lang.reflect.Method;

public class MiUiNotchScreen implements INotchSupport {
    @Override
    public boolean isNotchScreen(Window window) {
//        刘海屏检测
        return "1".equals((String)SystemProperties.getInstance().get("ro.miui.notch"));
    }
// 刘海屏  高度
    @Override
    public int getNotchHeight(Window window) {
        int result = 0;
        if (!isNotchScreen(window)) {
            return 0;
        }
        if (isHideNotch(window.getContext())) {
            result= NotchStatusBarUtils.getStatusBarHeight(window.getContext());
        } else {
            result = getRealNotchHeight(window.getContext());
        }
        return 0;
    }

    private int getRealNotchHeight(Context context) {
//        所有
        int result = 0;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }else {
            result = NotchStatusBarUtils.getStatusBarHeight(context);
        }
//        小米 非miui10
        return result;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isHideNotch(Context activity) {
        return Settings.Global.getInt(activity.getContentResolver(), "force_black", 0) == 1;
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(activity.getWindow(), flag);
        } catch (Exception e) {
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
