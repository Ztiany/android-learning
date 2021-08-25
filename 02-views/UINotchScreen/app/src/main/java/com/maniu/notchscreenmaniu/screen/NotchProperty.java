package com.maniu.notchscreenmaniu.screen  ;

public class NotchProperty {

    /**
     * 刘海高度
     */
    private int notchHeight;
    /**
     * 是否是刘海屏
     */
    private boolean isNotch;

    private int statusBarHeight;

    private int marginTop;

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getNotchHeight() {
        return notchHeight;
    }

    public void setNotchHeight(int notchHeight) {
        this.notchHeight = notchHeight;
    }

    public boolean isNotch() {
        return isNotch;
    }

    public void setNotch(boolean notch) {
        isNotch = notch;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }
}
