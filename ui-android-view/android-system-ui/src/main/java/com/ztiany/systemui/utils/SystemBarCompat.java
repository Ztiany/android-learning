package com.ztiany.systemui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ztiany.systemui.R;


/**
 * A tool for adjusting system bars.
 *
 * <p>
 * other useful libs:
 * <ol>
 * <li>https://github.com/Zackratos/UltimateBarX</li>
 * <li>https://github.com/Veaer/Glass</li>
 * <li>https://github.com/H07000223/FlycoSystemBar</li>
 * <li>https://github.com/niorgai/StatusBarCompat</li>
 * <li>https://github.com/laobie/StatusBarUtil</li>
 * <li>https://github.com/msdx/status-bar-compat</li>
 * </ol>
 * </p>
 *
 * <p>
 * other useful utils:
 * <ol>
 * <li>{@link androidx.core.view.ViewCompat}</li>
 * <li>{@link androidx.core.view.WindowCompat}</li>
 * <li>{@link androidx.core.view.WindowInsetsCompat}</li>
 * <li>{@link androidx.core.view.WindowInsetsControllerCompat}</li>
 * </ol>
 * </p>
 *
 * @author Ztiany
 * Date :   2016-03-16 21:52
 */
public class SystemBarCompat {

    private SystemBarCompat() {
        throw new UnsupportedOperationException();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Full Screen
    ///////////////////////////////////////////////////////////////////////////

    public static void setFullScreen(@NonNull Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    ///////////////////////////////////////////////////////////////////////////
    // setDecorFitsSystemWindows
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 让布局延伸至状态栏与导览区域，将状态栏和导航栏的颜色设置为透明色。
     */
    public static void setDecorFitsSystemWindows(Activity activity) {
        setDecorFitsSystemWindowsInternal(activity, true, true);
    }

    private static void setDecorFitsSystemWindowsInternal(Activity activity, boolean status, boolean navigation) {
        Window window = activity.getWindow();
        if (AndroidVersion.atLeast(30)) {
            WindowCompat.setDecorFitsSystemWindows(window, !(status || navigation));
            setStatusBarColorAfter19(activity, Color.TRANSPARENT);
            setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
        } else if (AndroidVersion.atLeast(21)) {
            if (navigation && status) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
                setStatusBarColorAfter19(activity, Color.TRANSPARENT);
                setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
            } else if (status) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                setStatusBarColorAfter19(activity, Color.TRANSPARENT);
            } else if (navigation) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                setNavigationBarColorAfter19(activity, Color.TRANSPARENT);
            }
        } else if (AndroidVersion.at(19)) {
            setTranslucentSystemBar(window, status, navigation);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SystemBar Color
    ///////////////////////////////////////////////////////////////////////////

    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        setStatusBarColorOn19(activity, color);
        setStatusBarColorAfter19(activity, color);
    }

    public static void setNavigationBarColor(Activity activity, @ColorInt int color) {
        setNavigationBarColorAfter19(activity, color);
    }

    private static void setStatusBarColorAfter19(Activity activity, @ColorInt int color) {
        if (!AndroidVersion.above(20)) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(color);
    }

    private static void setNavigationBarColorAfter19(Activity activity, @ColorInt int color) {
        if (!AndroidVersion.above(20)) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
    }

    private static View setStatusBarColorOn19(Activity activity, @ColorInt int color) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        return setupStatusBarViewOn19(activity, decorView, color);
    }

    /**
     * 适用于 Android 4.4，在 rootView 中添加一个与 StatusBar 高度一样的 View，用于对状态栏着色。
     *
     * @param activity 上下文
     * @param rootView 用于添加着色 View 的根 View
     * @param color    着色
     * @return 被添加的 View
     */
    @SuppressWarnings("WeakerAccess")
    private static View setupStatusBarViewOn19(Activity activity, ViewGroup rootView, @ColorInt int color) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
            return null;
        }
        View statusBarTintView = rootView.findViewById(R.id.base_status_view_id);
        if (statusBarTintView == null) {
            statusBarTintView = new View(activity);
            statusBarTintView.setId(R.id.base_status_view_id);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            layoutParams.gravity = Gravity.TOP;
            statusBarTintView.setLayoutParams(layoutParams);
            rootView.addView(statusBarTintView, 0);
        }
        statusBarTintView.setBackgroundColor(color);
        return statusBarTintView;
    }

    private static void setTranslucentSystemBar(Window win, boolean status, boolean navigation) {
        if (!AndroidVersion.atLeast(19)) {
            return;
        }
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (status) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (navigation) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SystemBar height
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取状态栏高度，如果状态栏没有展示则返回 0。
     */
    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        /*
            1. 该方法返回分发给视图树的原始 insets
            2. Insets 只有在 view attached 才是可用的
            3. API 20 及以下 永远 返回 false
         */
        WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
        if (windowInsets != null) {
            statusBarHeight = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars()).top;
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeightIgnoreVisibility(Activity activity) {
        int statusBarHeight = 0;
        @SuppressLint("InternalInsetResource")
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }

        if (statusBarHeight <= 0) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
            if (windowInsets != null) {
                statusBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            }
        }

        return statusBarHeight;
    }

    /**
     * 获取 NavigationBar 高度，如果 NavigationBar 没有展示则返回 0。
     */
    public static int getNavigationBarHeight(Activity activity) {
        int navigationBarHeight = 0;
        WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
        if (windowInsets != null) {
            navigationBarHeight = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars()).bottom;
        }
        return navigationBarHeight;
    }

    /**
     * 获取 NavigationBar 高度
     */
    public static int getNavigationBarHeightIgnoreVisibility(Activity activity) {
        int navigationBarHeight = 0;
        Resources rs = activity.getResources();
        @SuppressLint("InternalInsetResource")
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }

        if (navigationBarHeight <= 0) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
            if (windowInsets != null) {
                navigationBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            }
        }

        return navigationBarHeight;
    }

    /**
     * 是否存在 NavigationBar。
     * <br/><br/>
     * 一些老的实现可以参考：
     *
     * <ol>
     *     <li> <a href='https://stackoverflow.com/questions/28983621/detect-soft-navigation-bar-availability-in-android-device-progmatically'>detect-soft-navigation-bar-availability-in-android-device-progmatically</a> </li>
     *     <li> <a href='https://windysha.github.io/2018/02/07/Android-APP%E9%80%82%E9%85%8D%E5%85%A8%E9%9D%A2%E5%B1%8F%E6%89%8B%E6%9C%BA%E7%9A%84%E6%8A%80%E6%9C%AF%E8%A6%81%E7%82%B9/'>Android APP适配全面屏手机的技术要点</a> </li>
     *     <li> <a href='https://github.com/roughike/BottomBar/blob/master/bottom-bar/src/main/java/com/roughike/bottombar/NavbarUtils.java'>NavbarUtils</a> </li>
     * </ol>
     * <p>
     * 现在可以通过 {@link androidx.core.view.WindowInsetsCompat} 来判断，具体可以参考 <a href='https://juejin.cn/post/7038422081528135687'>Android Detail：Window 篇—— WindowInsets 与 fitsSystemWindow</a>
     */
    public static boolean hasNavigationBar(Activity activity) {
        return getNavigationBarHeight(activity) > 0;
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity activity
     * @return ActionBar高度
     */
    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Notch
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @see <a href='https://developer.android.com/guide/topics/display-cutout?hl=zh-cn'>支持刘海屏</a>
     * @see <a href='https://juejin.im/post/5cf635846fb9a07f0c466ea7'>Android刘海屏、水滴屏全面屏适配方案</a>
     */
    public static void displayInNotch(Activity activity) {
        if (AndroidVersion.atLeast(28)) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(attributes);
        }
    }

}