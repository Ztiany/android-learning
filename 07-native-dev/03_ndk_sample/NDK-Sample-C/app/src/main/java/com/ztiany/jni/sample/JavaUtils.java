package com.ztiany.jni.sample;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-11-23 16:13
 */
public class JavaUtils {

    public JavaUtils(String message) {

    }

    /**
     * java冒泡排序
     *
     * @param arr 数组
     */
    private static void javaBubbleSort(int[] arr) {
        //冒泡排序
        for (int x = 0; x < arr.length - 1; x++) {
            for (int y = 0; y < arr.length - x - 1; y++) {
                if (arr[y] > arr[y + 1]) {
                    int temp = arr[y];
                    arr[y] = arr[y + 1];
                    arr[y + 1] = temp;
                }
            }
        }
    }

}
