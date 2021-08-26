package com.ztiany.jni.sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private PosixThread mPosixThread;

    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        System.loadLibrary("native-lib");
    }

    private JniBridge mJniBridge;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.<TextView>findViewById(R.id.jniTvInfo).setText("isArtInUse = " + getIsArtInUse() + ", getCurrentRuntimeValue = " + getCurrentRuntimeValue()+" abi = "+ Build.CPU_ABI);

        mJniBridge = new JniBridge();
        mPosixThread = new PosixThread();
        mPosixThread.init();
    }

    private boolean getIsArtInUse() {
        final String vmVersion = System.getProperty("java.vm.version");
        return vmVersion != null && vmVersion.startsWith("2");
    }

    private CharSequence getCurrentRuntimeValue() {
        final String SELECT_RUNTIME_PROPERTY = "persist.sys.dalvik.vm.lib";
        final String LIB_DALVIK = "libdvm.so";
        final String LIB_ART = "libart.so";
        final String LIB_ART_D = "libartd.so";
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get", String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String) get.invoke(systemProperties, SELECT_RUNTIME_PROPERTY,/* Assuming default is */"Dalvik");
                    if (LIB_DALVIK.equals(value)) {
                        return "Dalvik";
                    } else if (LIB_ART.equals(value)) {
                        return "ART";
                    } else if (LIB_ART_D.equals(value)) {
                        return "ART debug build";
                    }
                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }

    //返回字符串
    public void stringFromC(View view) {
        String stringFromC = JniBridge.stringFromC();
        AppContext.showToast(stringFromC);
    }

    public void stringFromCReflection(View view) {
        String stringFromC = JniBridge.stringFromCReflection();
        AppContext.showToast(stringFromC);
    }

    //模拟登录
    public void intFromC(View view) {
        int a = 10;
        int b = 20;
        AppContext.showToast("intFromC: " + (mJniBridge.intFromC(a, b)));
    }

    //修改每个元素后返回
    public void addArray(View view) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mJniBridge.addArray(arr, 100);
        Log.d(TAG, "addArray: " + Arrays.toString(arr));
    }

    //c语言的冒泡排序
    public void bubbleSort(View view) {
        int[] originArr = initIntArr();
        Log.d(TAG, "originArr: " + Arrays.toString(originArr));
        long start = System.currentTimeMillis();
        mJniBridge.bubbleSort(originArr);
        Log.d(TAG, "originArr: " + Arrays.toString(originArr));
        Log.d(TAG, "bubbleSort use time: " + (System.currentTimeMillis() - start));
    }

    //加密
    public void encryption(View view) {
        String password = "Java password";
        String encryption = mJniBridge.encryption(password);
        Log.d(TAG, "加密 " + encryption);
    }

    //让C调用Java
    public void callJava(View view) {
        mJniBridge.callJava("Java message");
    }

    //让C抛出异常
    public void nativeThrowError(View view) {
        try {
            mJniBridge.throwError("抛出一个异常吧");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nativeThrowErrorUnCaught(View view) {
        mJniBridge.triggerSignal();
    }

    //调用C动态方法注册
    public void dynamicRegisterFromJni(View view) {
        String registerFromJni = mJniBridge.dynamicRegisterFromJni();
        AppContext.showToast(registerFromJni);
    }

    private int[] initIntArr() {
        int[] arr = new int[10000];
        for (int x = 0; x < arr.length; x++) {
            arr[x] = (int) (Math.random() * 10000 + 1);
        }
        return arr;
    }

    public void pThread(View view) {
        mPosixThread.pthread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPosixThread.destroy();
    }

}
