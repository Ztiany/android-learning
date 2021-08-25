package com.weishu.upf.dynamic_proxy_hook.app2.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author weishu
 * @date 16/1/28
 */
public class HookHelper {

    private static final String TAG = HookHelper.class.getSimpleName();
    private static EvilInstrumentation sEvilInstrumentation;

    public static void attachContext() throws Exception {
        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
        Log.d(TAG, "mInstrumentation:" + mInstrumentation);

        // 创建代理对象
        sEvilInstrumentation = new EvilInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, sEvilInstrumentation);
    }

    public static void attachContext(Activity activity) {
        try {
            Field mInstrumentationFiled = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentationFiled.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentationFiled.get(activity);
            Log.d(TAG, "instrumentation:" + instrumentation);
            if (sEvilInstrumentation.getBase() == instrumentation) {
                mInstrumentationFiled.set(activity, sEvilInstrumentation);
            } else {
                mInstrumentationFiled.set(activity, new EvilInstrumentation(instrumentation));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
