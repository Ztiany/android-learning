package com.ztiany.androidipc.service.binderpool;

import android.os.RemoteException;
import android.util.Log;

import com.ztiany.androidipc.ISecurityCenter;

public class SecurityCenter extends ISecurityCenter.Stub {

    public static final char SECRET_CODE = '^';

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public String encrypt(String content) throws RemoteException {
        if (content == null) {
            return null;
        }
        char[] chars = content.toCharArray();
        int i = chars.length;
        for (int j = 0 ; j < i ; j ++) {
            chars[j] ^= SECRET_CODE;
        }
        Log.d("Compute", "Thread.currentThread():" + Thread.currentThread().getName());

        return new String(chars);
    }

    @Override
    public String decrypt(String pwd) throws RemoteException {
        return encrypt(pwd);
    }




}
