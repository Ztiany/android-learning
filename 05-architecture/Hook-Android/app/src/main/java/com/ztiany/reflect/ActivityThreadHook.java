package com.ztiany.reflect;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-12-28 23:28
 */
public class ActivityThreadHook {

    private static final String TAG = ActivityThreadHook.class.getSimpleName();

    private static volatile ActivityThreadHook sActivityThreadHook;
    private Object mActivityThread;
    private Class<?> mActivityThreadClass;

    public static ActivityThreadHook getInstance() {
        if (sActivityThreadHook == null) {
            synchronized (ActivityThreadHook.class) {
                if (sActivityThreadHook == null) {
                    sActivityThreadHook = new ActivityThreadHook();
                }
            }
        }
        return sActivityThreadHook;
    }

    private ActivityThreadHook() {
    }

    public void hookActivityThread() {
        try {
            mActivityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = mActivityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            //获取主线程对象
            mActivityThread = currentActivityThreadMethod.invoke(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        hookActivityThreadHandler();
        hookInstrumentation();
    }

    private void hookActivityThreadHandler() {
        //获取mH字段
        Field mH;
        try {
            mH = mActivityThreadClass.getDeclaredField("mH");
            mH.setAccessible(true);
            //获取Handler
            Handler handler = (Handler) mH.get(mActivityThread);
            //获取原始的mCallBack字段
            Field mCallBack = Handler.class.getDeclaredField("mCallback");
            mCallBack.setAccessible(true);
            //这里设置了我们自己实现了接口的CallBack对象
            mCallBack.set(handler, new UserHandler(handler));
            Log.d(TAG, "Hook mH成功");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void hookInstrumentation() {
        try {
            //获取Instrumentation字段
            Field mInstrumentation = mActivityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(mActivityThread);
            CustomInstrumentation customInstrumentation = new CustomInstrumentation(instrumentation);
            //替换掉原来的,就是把系统的instrumentation替换为自己的Instrumentation对象
            mInstrumentation.set(mActivityThread, customInstrumentation);
            Log.d(TAG, "Hook Instrumentation成功");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static class CustomInstrumentation extends Instrumentation {

        private static final String TAG = CustomInstrumentation.class.getSimpleName();

        private Instrumentation base;

        CustomInstrumentation(Instrumentation base) {
            this.base = base;
        }

        //重写创建Activity的方法
        @Override
        public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
            Log.d(TAG, "newActivity() called with: cl = [" + cl + "], className = [" + className + "], intent = [" + intent + "]");
            return base.newActivity(cl, className, intent);
        }
    }

    private static class UserHandler implements Handler.Callback {

        private static final String TAG = UserHandler.class.getSimpleName();

        //这个100一般情况下最好也反射获取，这里简单写死，保持跟源码一致即可
        static final int LAUNCH_ACTIVITY = 100;
        private Handler origin;

        UserHandler(Handler mHandler) {
            this.origin = mHandler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == LAUNCH_ACTIVITY) {
                Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
            }
            origin.handleMessage(msg);
            return true;
        }
    }

}
