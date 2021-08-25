package com.ryg.dynamicload.sample.mainhost;

import android.content.Context;
import android.widget.Toast;

/**
 * 测试有插件调用宿主方法
 */
public class TestHostClass {

    public void testMethod(Context context) {
        Toast.makeText(context, "Successed invoke host method", Toast.LENGTH_SHORT).show();
    }
}
