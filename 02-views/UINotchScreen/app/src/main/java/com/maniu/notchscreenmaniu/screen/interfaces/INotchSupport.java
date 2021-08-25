package com.maniu.notchscreenmaniu.screen.interfaces;

import android.app.Activity;
import android.view.Window;

public interface INotchSupport {

    /**
     * 判断当前是否是刘海屏手机
     * @param window
     * @return
     */
    boolean isNotchScreen(Window window);


    /**
     * 获取刘海屏的高度
     * @param window
     * @return
     */
    int getNotchHeight(Window window);

//    获取的值   每个手机厂商   看 api

//  1   用刘海屏内的内容


    /**
     * 隐藏状态栏，全屏状态下，使用状态栏区域
     * @param activity
     */
    void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack);
//  2   不用刘海屏内的内容  透明的  设置颜色

    void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack);





}
