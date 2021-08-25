package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.view.DisplayCutout;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

public class CommonNotchScreen implements INotchSupport {
    @Override
    public boolean isNotchScreen(Window window) {
//        检测  Google官方的方式   david一劳永逸   google
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return false;
        }
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if(displayCutout == null || displayCutout.getBoundingRects() == null){
            return false;
        }
        return true;
    }

    @Override
    public int getNotchHeight(Window window) {
        int notchHeight = 0;
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return 0;
        }

        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if(displayCutout == null || displayCutout.getBoundingRects() == null){
            return 0;
        }
        notchHeight = displayCutout.getSafeInsetTop();
        return notchHeight;
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow());
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        activity.getWindow().setAttributes(attributes);
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
//        设置全面屏
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow());
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        activity.getWindow().setAttributes(attributes);
//        view 黑色的View 放上去
        NotchStatusBarUtils.showFakeNotchView(activity.getWindow(), getNotchHeight(activity.getWindow()));





    }
}
