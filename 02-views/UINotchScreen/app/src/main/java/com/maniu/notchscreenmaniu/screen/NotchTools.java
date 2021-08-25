package com.maniu.notchscreenmaniu.screen;

import android.app.Activity;
import android.view.View;

import com.maniu.notchscreenmaniu.screen.interfaces.INotchSupport;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

public class NotchTools {
    private INotchSupport notchScreenSupport;

    public    void fullScreenUseNotch(final Activity activity, final OnNotchCallBack notchCallBack) {

        fullScreenUseStatus(activity, notchCallBack);

    }
    public    void fullScreenUseStatus(final Activity activity, final OnNotchCallBack notchCallBack) {
        checkNotch();
        activity.getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                notchScreenSupport.fullScreenUseStatus(activity, notchCallBack);
            }
            @Override
            public void onViewDetachedFromWindow(View view) {
            }
        });
    }
    public void fullScreenDontUseNotch (final Activity activity) {
        checkNotch();
        activity.getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                notchScreenSupport.fullScreenDontUseStatus(activity, null);
            }
            @Override
            public void onViewDetachedFromWindow(View view) {
            }
        });
    }

    public void checkNotch() {
        if (notchScreenSupport == null) {
            if (DeviceTools.isMiui()) {
                notchScreenSupport = new MiUiNotchScreen();
            }else if (DeviceTools.isHuaWei()) {
                notchScreenSupport = new HuaWeiNotchScreen();
            } else if (DeviceTools.isVivo()) {
                notchScreenSupport = new VivoNotchScreen();
            } else if (DeviceTools.isOppo()) {
                notchScreenSupport = new OppoNotchScreen();
            } else {
                notchScreenSupport = new CommonNotchScreen();
            }
        }
    }
}
