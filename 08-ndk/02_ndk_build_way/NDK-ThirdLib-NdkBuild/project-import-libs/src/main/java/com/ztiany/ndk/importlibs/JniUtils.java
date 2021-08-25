package com.ztiany.ndk.importlibs;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-11-04 23:07
 */
class JniUtils {

    public native String stringFromJNI();

    static {
        System.loadLibrary("hello-libs");
    }
}
