package com.ztiany.sample2;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-29 16:10
 */
class JniUtils {

    static {
        System.loadLibrary("Hello");
    }

    public native String getMessage();

}
