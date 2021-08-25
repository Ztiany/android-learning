package com.maniu.notchscreenmaniu.screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class NotchStatusBarUtils {
    private static int statusBarHeight = -1;
//    oppo +vivo   设置 google
    public static void setFullScreenWithSystemUi(final Window window) {
        int systemUiVisibility = 0;
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight < 0) {
            int result = 0;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object obj = clazz.newInstance();
                Field field = clazz.getField("status_bar_height");
                int resourceId1 = Integer.parseInt(field.get(obj).toString());
                result = context.getResources().getDimensionPixelSize(resourceId1);
            } catch (Exception e) {
            } finally {
                statusBarHeight = result;
            }
        }
        return statusBarHeight;
    }
    public static void showFakeNotchView(Window window,int height) {
        View decorView = window.getDecorView();
        ViewGroup notchContainer =decorView.findViewWithTag("notch_container");
        if (notchContainer == null) {
            return;
        }

        if (notchContainer.getChildCount() == 0) {
            View view = new View(window.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    height));
            view.setBackgroundColor(Color.BLACK);
            notchContainer.addView(view);
        }
        notchContainer.setVisibility(View.VISIBLE);


    }
}
