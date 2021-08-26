package com.ztiany.ndk.two_libs;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-31 23:58
 */
class JniUtils {

    static {
        System.loadLibrary("Math");
    }

    public native int add(int a, int b);

}
