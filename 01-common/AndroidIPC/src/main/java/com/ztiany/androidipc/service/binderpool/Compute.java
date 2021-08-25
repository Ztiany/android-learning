package com.ztiany.androidipc.service.binderpool;

import android.os.RemoteException;
import android.util.Log;

import com.ztiany.androidipc.ICompute;

public class Compute extends ICompute.Stub  {

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public int add(int a, int b) throws RemoteException {
        Log.d("Compute", "Thread.currentThread():" + Thread.currentThread().getName());
        return a + b;
    }
}
