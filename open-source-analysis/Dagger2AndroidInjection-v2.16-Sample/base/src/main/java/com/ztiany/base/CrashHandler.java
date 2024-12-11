package com.ztiany.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局异常处理
 */
class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private static final String TAG = CrashHandler.class.getSimpleName();

    public static CrashHandler register(Application application) {
        CrashHandler crashHandler = new CrashHandler(application);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        return crashHandler;
    }

    private CrashHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 收集异常信息，写入到sd卡
        restoreCrash(thread, ex);
        // 发送Crash信息到服务器
        sendCrashReport(thread, ex);
        // 处理异常
        handleException(thread, ex);
        //退出
        killProcess();
    }


    private void restoreCrash(@SuppressWarnings("unused") Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        // 收集异常信息，写入到sd卡
        File dir = new File(mContext.getExternalCacheDir() + File.separator + "crash");

        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                Log.e(TAG,"CrashHandler create dir fail");
                return;
            }
        }

        try {

            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String name = dateFormat.format(new Date(System.currentTimeMillis()))
                    + ".log";
            File fileName = new File(dir, name);
            if (!fileName.exists()) {
                @SuppressWarnings("unused")
                boolean newFile = fileName.createNewFile();
            }

            PrintStream err = new PrintStream(fileName);

            err.println("--------------------------------AppInfo--------------------------------");
            err.println();
            err.println();
            err.println("--------------------------------SystemInfo:--------------------------------");
            err.println("Product: " + android.os.Build.PRODUCT);
            err.println("CPU_ABI: " + android.os.Build.CPU_ABI);
            err.println("TAGS: " + android.os.Build.TAGS);
            err.println("VERSION_CODES.BASE:" + android.os.Build.VERSION_CODES.BASE);
            err.println("MODEL: " + android.os.Build.MODEL);
            err.println("SDK: " + Build.VERSION.SDK_INT);
            err.println("VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE);
            err.println("DEVICE: " + android.os.Build.DEVICE);
            err.println("DISPLAY: " + android.os.Build.DISPLAY);
            err.println("BRAND: " + android.os.Build.BRAND);
            err.println("BOARD: " + android.os.Build.BOARD);
            err.println("FINGERPRINT: " + android.os.Build.FINGERPRINT);
            err.println("ID: " + android.os.Build.ID);
            err.println("MANUFACTURER: " + android.os.Build.MANUFACTURER);
            err.println("USER: " + android.os.Build.USER);
            err.println();
            err.println();
            err.println("--------------------------------CrashContent--------------------------------");
            ex.printStackTrace(err);
            err.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void sendCrashReport(Thread thread, Throwable ex) {
    }

    @SuppressWarnings("unused")
    private void handleException(Thread thread, Throwable ex) {
    }

    @SuppressWarnings("unused")
    private void killProcess() {
        Process.killProcess(Process.myPid());
    }
}