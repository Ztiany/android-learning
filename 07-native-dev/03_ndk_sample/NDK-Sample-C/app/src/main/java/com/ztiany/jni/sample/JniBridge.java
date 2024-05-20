package com.ztiany.jni.sample;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-11-23 16:09
 */
public class JniBridge {

    //返回字符串
    public static native String stringFromC();

    public static native String stringFromCReflection();

    //模拟登录
    public native int intFromC(int a, int b);

    //修改每个元素后返回
    public native int[] addArray(int origin[], int add);

    //c语言的冒泡排序
    public native void bubbleSort(int[] arr);

    //加密
    public native String encryption(String password);

    //让C调用Java
    public native void callJava(String message);

    //让C抛出异常
    public native void throwError(String message);

    //制造信号
    public native void triggerSignal();

    //调用C动态方法注册
    public native String dynamicRegisterFromJni();

    public static void showMessage(String message) {
        AppContext.showToast(message);
    }

}
