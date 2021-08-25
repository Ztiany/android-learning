package com.ztiany.androidaspectj03.sample4;

import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-28 15:12
 */
public class CflowTarget {

    private static final String TAG = CflowTarget.class.getSimpleName();

    private void foo() {
        Log.d(TAG, "foo......");
        Log.d(TAG, "foo......");
        Log.d(TAG, "foo......");
        Log.d(TAG, "foo......");
    }

    private void bar() {
        foo();
        Log.d(TAG, "bar.........");
    }

    public void testCflow() {
        bar();
        foo();
        calc();
    }

    private void calc() {
        int a = 123;
        int b = 321;
        String name = "ztiany";
        int result = a + b;
        String finalResult = name + result;
        Log.d(TAG, finalResult);
    }


}
