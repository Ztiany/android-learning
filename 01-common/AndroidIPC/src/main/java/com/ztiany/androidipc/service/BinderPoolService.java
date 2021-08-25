package com.ztiany.androidipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ztiany.androidipc.IBinderPool;
import com.ztiany.androidipc.service.binderpool.BinderPool;

public class BinderPoolService extends Service {

    private IBinderPool.Stub mStub = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

}
