package com.ztiany.view.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-15 12:23
 */
public class ViewUtils {

    public static long getAnimationTotalTime(AnimationDrawable animationDrawable) {
        long time = 0;
        int numberOfFrames = animationDrawable.getNumberOfFrames();
        for (int i = 0; i < numberOfFrames; i++) {
            time += animationDrawable.getDuration(i);
        }
        return time;
    }

    public static Bitmap getBitmapFromView(View originalView) {
        originalView.setDrawingCacheEnabled(true);
        originalView.buildDrawingCache();
        return originalView.getDrawingCache();
    }
}
