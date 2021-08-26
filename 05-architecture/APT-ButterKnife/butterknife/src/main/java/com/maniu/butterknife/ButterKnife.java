package com.maniu.butterknife;


import java.lang.reflect.Constructor;

public class ButterKnife {
    public static void bind(Object activity){
        //获取到类名
        String name = activity.getClass().getName();
        //获取到注解处理器所对应的这个组件的类对象的名字
        String binderName = name+"$ViewBinder";
        try {
            Class<?> aClass = Class.forName(binderName);
            //获取到构造方法
            Constructor<?> constructor = aClass.getConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
