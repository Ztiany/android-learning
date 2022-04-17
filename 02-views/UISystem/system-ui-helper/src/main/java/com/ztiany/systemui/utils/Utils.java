package com.ztiany.systemui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-07-18 21:36
 */
public class Utils {

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

    @SuppressLint("ObsoleteSdkInt")
    public static void printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                sb.append("\nSERIAL             :").append(Build.SERIAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timber.tag("DEVICES").i(sb.toString());
    }


}
