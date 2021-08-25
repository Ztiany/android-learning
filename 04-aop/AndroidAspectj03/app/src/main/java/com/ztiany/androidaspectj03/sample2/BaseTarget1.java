package com.ztiany.androidaspectj03.sample2;

import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-27 16:12
 */
public class BaseTarget1 {

    private static final String TAG = BaseTarget1.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////
    // test 1
    ///////////////////////////////////////////////////////////////////////////
    public void testAround() {
        String ztiany = login("ztiany", "123456");
        saveUserInfo(ztiany);
        showUserInfo(ztiany);
    }

    private String login(String name, String password) {
        Log.d(TAG, "login() called with: name = [" + name + "], password = [" + password + "]");
        return name;
    }

    private void saveUserInfo(String info) {
        Log.d(TAG, "saveUserInfo() called with: info = [" + info + "]");
    }

    private void showUserInfo(String ztiany) {
        Log.d(TAG, "showUserInfo() called with: ztiany = [" + ztiany + "]");
    }

    ///////////////////////////////////////////////////////////////////////////
    // test 4
    ///////////////////////////////////////////////////////////////////////////
    public void withInSample() {
        Log.d(TAG, "withInSample() called in BaseTarget2");
    }
}
