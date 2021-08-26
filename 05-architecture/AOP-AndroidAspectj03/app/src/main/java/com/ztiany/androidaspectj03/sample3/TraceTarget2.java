package com.ztiany.androidaspectj03.sample3;

import android.os.SystemClock;
import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-28 14:21
 */
public class TraceTarget2 {

    private static final String TAG = TraceTarget2.class.getSimpleName();

    // java.lang.ClassCastException: org.aspectj.runtime.reflect.ConstructorSignatureImpl cannot be cast to org.aspectj.lang.reflect.MethodSignature
    /*public TraceTarget2() {

    }*/

    public void run() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                method1();
            }
        }.start();
    }

    private void method1() {
        SystemClock.sleep(1000);
        Log.d(TAG, "test1() called");
        method2();
        method3();
    }

    private void method3() {
        SystemClock.sleep(100);
        Log.d(TAG, "method3() called");
    }

    private void method2() {
        SystemClock.sleep(200);
        Log.d(TAG, "method2() called");
    }

}
