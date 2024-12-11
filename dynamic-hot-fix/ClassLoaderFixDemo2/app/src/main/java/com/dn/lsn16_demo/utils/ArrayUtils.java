package com.dn.lsn16_demo.utils;

import java.lang.reflect.Array;

public class ArrayUtils {
    /**
     * 合并数组
     *
     * @param arrayLhs 前数组（插队数组）
     * @param arrayRhs 后数组（已有数组）
     * @return 处理后的新数组
     */
    public static Object combineArray(Object arrayLhs, Object arrayRhs) {
        // 获得一个数组的Class对象，通过Array.newInstance()可以反射生成数组对象
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        // 前数组长度
        int i = Array.getLength(arrayLhs);
        // 新数组总长度 = 前数组长度 + 后数组长度
        int j = i + Array.getLength(arrayRhs);
        // 生成数组对象
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            //先把自己的放入数组
            if (k < i) {
                // 从0开始遍历，如果前数组有值，添加到新数组的第一个位置
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                // 添加完前数组，再添加后数组，合并完成
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }
}
