package com.ztiany.systemui;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-18 21:36
 */
public class Utils {

    private Utils() {

    }

    public static void printSize(final Activity activity) {

        final String TAG = activity.getClass().getSimpleName();

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        Log.d(TAG, "displayMetrics.widthPixels:" + displayMetrics.widthPixels);
        Log.d(TAG, "displayMetrics.heightPixels:" + displayMetrics.heightPixels);
        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "activity.getWindow().getDecorView().getMeasuredHeight():" + activity.getWindow().getDecorView().getMeasuredHeight());
                Log.d(TAG, "activity.getWindow().getDecorView().getMeasuredWidth():" + activity.getWindow().getDecorView().getMeasuredWidth());
            }
        });
    }
}
