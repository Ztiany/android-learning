package com.ztiany.jni.sample;

import java.util.Arrays;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 2017/4/5 22:12
 */
public class JNIMain {

    private JniBridge mJniBridge = new JniBridge();

    static {
        System.loadLibrary("jni/native");
    }

    //generate windows dll：
    //gcc -Wl,--add-stdcall-alias -I "include" -I "include\win32"  -shared -o native.dll native-lib.c Utils.c FileUtils.c
    public static void main(String... args) {
        JNIMain jniMain = new JNIMain();
        jniMain.encryption();
        jniMain.splitFile();
        jniMain.mergeFile();
    }


    //返回字符串
    private void stringFromC() {
        String stringFromC = JniBridge.stringFromC();
        System.out.println(stringFromC);
    }

    //模拟登录
    private void intFromC() {
        int a = 10;
        int b = 20;
        System.out.println("intFromC: " + (mJniBridge.intFromC(a, b)));
    }

    //修改每个元素后返回
    private void addArray() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mJniBridge.addArray(arr, 100);
        System.out.println("addArray: " + Arrays.toString(arr));
    }

    //c语言的冒泡排序
    private void bubbleSort() {
        int[] originArr = initIntArr();
        System.out.println("originArr: " + Arrays.toString(originArr));
        long start = System.currentTimeMillis();
        mJniBridge.bubbleSort(originArr);
        System.out.println("originArr: " + Arrays.toString(originArr));
        System.out.println("bubbleSort use time: " + (System.currentTimeMillis() - start));
    }

    //加密
    private void encryption() {
        String password = "Java 哈哈->";
        String encryption = mJniBridge.encryption(password);
        System.out.println("加密 " + encryption);
    }

    //让C调用Java
    private void callJava() {
        mJniBridge.callJava("Java message");
    }

    //让C抛出异常
    private void throwError() {
        try {
            mJniBridge.throwError("抛出一个异常吧");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //调用C动态方法注册
    private void dynamicRegisterFromJni() {
        String registerFromJni = mJniBridge.dynamicRegisterFromJni();
        System.out.println(registerFromJni);
    }

    private int[] initIntArr() {
        int[] arr = new int[10000];
        for (int x = 0; x < arr.length; x++) {
            arr[x] = (int) (Math.random() * 10000 + 1);
        }
        return arr;
    }

    private void splitFile() {
        //分割文件
        final String path = "E:/code/studio/my_github/Repository/Java/JNI/file/size.exe";
        final String pathPattern = "E:/code/studio/my_github/Repository/Java/JNI/file/size_%d.exe";
        mJniBridge.splitFile(path, pathPattern, 3);
    }

    private void mergeFile() {
        //合并文件
        final String fileNewPath = "E:/code/studio/my_github/Repository/Java/JNI/file/size_new.exe";
        final String pathPattern = "E:/code/studio/my_github/Repository/Java/JNI/file/size_%d.exe";
        mJniBridge.mergeFile(fileNewPath, pathPattern, 3);
    }


}
