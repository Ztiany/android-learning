package com.ztiany.androidaspectj03.sample2;

import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-27 16:12
 */
public class BaseTarget2 {

    private static final String TAG = BaseTarget2.class.getSimpleName();


    ///////////////////////////////////////////////////////////////////////////
    // test 2
    ///////////////////////////////////////////////////////////////////////////
    public void throwError() {
        int a = 0;
        int b = 3;
        int result = b / a;
    }

    ///////////////////////////////////////////////////////////////////////////
    // test 3
    ///////////////////////////////////////////////////////////////////////////
    public void print1() {
        doPrint("print 1 called");
    }

    public void print2() {
        doPrint("print 2 called");
    }

    private void doPrint(String abc) {
        Log.d(TAG, "doPrint() called with: abc = [" + abc + "]");
    }

    ///////////////////////////////////////////////////////////////////////////
    // test 4
    ///////////////////////////////////////////////////////////////////////////
    public void withInSample() {
        Log.d(TAG, "withInSample() called in BaseTarget1");
    }

    ///////////////////////////////////////////////////////////////////////////
    // test 5
    ///////////////////////////////////////////////////////////////////////////
    public void diffCallAndExecution() {
        Log.d(TAG, "diffCallAndExecution() called");
    }

}
