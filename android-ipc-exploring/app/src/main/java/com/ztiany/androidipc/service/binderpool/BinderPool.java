package com.ztiany.androidipc.service.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ztiany.androidipc.IBinderPool;
import com.ztiany.androidipc.service.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * 简单说明BinderPool采用单例实现，保证一个进程中只有一个对象，在初始化的时候就进行bindService，
 * 采用CountDownLatch来保证返回对象钱连接已经成功(即同步)，内部实现IBinderPool.Sub,让服务端Service返回IBinderPool.Sub，
 * 返回客户端可以通过IBinderPool.Sub进行其他业务模块的AIDL查询。并且处理好Service的异常重连。
 */
public class BinderPool {

    public static final String TAG = BinderPool.class.getSimpleName();

    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY = 1;

    public Context mContext;
    public IBinderPool mIBinderPool;
    public static volatile BinderPool sInstance;
    private CountDownLatch mCountDownLatch;

    public BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private void connectBinderPoolService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            Log.d(TAG, "await");
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "binder died");
            mIBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mIBinderPool = null;
            connectBinderPoolService();
        }
    };

    public IBinder queryBinder(int binderCode) {
        try {
            return mIBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mIBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.d(TAG, "mServiceConnection RemoteException");
            }
            Log.d(TAG, "countDown");
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "name:" + name);
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) {
            switch (binderCode) {
                case BINDER_COMPUTE:
                    return new Compute();
                case BINDER_SECURITY:
                    return new SecurityCenter();
            }
            return null;
        }
    }

}
