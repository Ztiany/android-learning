package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.util.Log;
import android.view.Window;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

public class OppoNotchScreen implements INotchSupport {
    @Override
    public boolean isNotchScreen(Window window) {
        boolean ret=window.getContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        return ret;
    }

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
            NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow());
        }
    }

    @Override
    public void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        this.fullScreenUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.showFakeNotchView(activity.getWindow(), getNotchHeight(activity.getWindow()));
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
//    回调用
}
