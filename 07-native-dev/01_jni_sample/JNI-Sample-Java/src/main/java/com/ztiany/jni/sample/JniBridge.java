package com.ztiany.jni.sample;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-11-23 16:09
 */
public class JniBridge {

    //返回字符串
    public static native String stringFromC();

    //加法
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

    //调用C动态方法注册
    public native String dynamicRegisterFromJni();

    public static void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * 拆分文件
     *
     * @param path         路径 E:\code\studio\my_github\Repository\Java\JNI\file\size.exe
     * @param path_pattern 拆分模式 E:\code\studio\my_github\Repository\Java\JNI\file\size_%d.exe
     * @param file_count   拆分为几个
     * @return 是否成功
     */
    public native boolean splitFile(String path, String path_pattern, int file_count);

    /**
     * 合并文件
     *
     * @param path         合并后的路径 E:\code\studio\my_github\Repository\Java\JNI\file\size.exe
     * @param path_pattern 文件之前拆分的模式 E:\code\studio\my_github\Repository\Java\JNI\file\size_%d.exe
     * @param file_count   有几个文件
     * @return 是否成功
     */
    public native boolean mergeFile(String path, String path_pattern, int file_count);
}
