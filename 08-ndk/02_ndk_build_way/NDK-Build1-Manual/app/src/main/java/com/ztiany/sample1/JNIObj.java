package com.ztiany.sample1;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-29 13:43
 */
class JNIObj {

    static {
        System.loadLibrary("Hello");
    }

    void invokeC(Context context) {
        Toast.makeText(context, getMessage(), Toast.LENGTH_SHORT).show();
    }

    public native String getMessage();

}
