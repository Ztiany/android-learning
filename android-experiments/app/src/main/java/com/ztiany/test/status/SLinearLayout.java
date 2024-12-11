package com.ztiany.test.status;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowInsets;
import android.widget.LinearLayout;


public class SLinearLayout extends LinearLayout {

    private static final String TAG = SLinearLayout.class.getSimpleName();

    public SLinearLayout(Context context) {
        super(context);
    }

    public SLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        Log.d(TAG, "fitSystemWindows() called with: insets = [" + insets + "]");
        boolean fitSystemWindows = super.fitSystemWindows(insets);
        Log.d(TAG, "fitSystemWindows:" + fitSystemWindows);
        return fitSystemWindows;
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        Log.d(TAG, "dispatchApplyWindowInsets() called with: insets = [" + insets + "]");
        return super.dispatchApplyWindowInsets(insets);
    }





}
