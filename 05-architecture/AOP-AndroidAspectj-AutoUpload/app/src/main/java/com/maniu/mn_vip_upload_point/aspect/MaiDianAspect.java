package com.maniu.mn_vip_upload_point.aspect;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.maniu.mn_vip_upload_point.annotation.CommonAnnotationBase;
import com.maniu.mn_vip_upload_point.annotation.PermasAnnotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 定义一个织入类，把这个类交给AspectJ的编译器来编译。
 */
@Aspect
public class MaiDianAspect {

    private static final String TAG = "MaiDianAspect";

    //定义切点  然后来进行匹配
    @Pointcut("execution(@com.maniu.mn_vip_upload_point.annotation.MaiDianData * *(..))")
    public void uploadMaiDian() {
    }

    @Around("uploadMaiDian()")
    public void executionSettingMaiDianData(ProceedingJoinPoint joinPoint) {
        //获取到方法的反射对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取到它上面的所有的注解
        Annotation[] annotations = method.getAnnotations();
        //获取到这个方法所有的接收的属性的注解
        Annotation[] methodParameterNamesByAnnotation = getMethodParameterNamesByAnnotation(method);
        if (methodParameterNamesByAnnotation == null || methodParameterNamesByAnnotation.length <= 0) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        //创建一个CommonAnnotationBase
        CommonAnnotationBase commonAnnotationBase = null;
        //遍历这个方法所有的注解
        for (Annotation annotation : annotations) {
            //获取到它的类型
            Class<?> annotationType = annotation.annotationType();
            commonAnnotationBase = annotationType.getAnnotation(CommonAnnotationBase.class);
            if (commonAnnotationBase == null) {
                break;
            }
        }
        if (commonAnnotationBase == null) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        //获取到注解携带的type以及ID
        String type = commonAnnotationBase.type();
        String actionId = commonAnnotationBase.actionId();
        Object[] args = joinPoint.getArgs();
        JSONObject jsonObject = getData(methodParameterNamesByAnnotation, args);
        //把收集起来的数据上传到服务器
        String msg = "上传埋点: " + "type: " + type + "  actionId:  " + actionId + "  data: " + jsonObject.toString();
        Log.e(TAG, msg);
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 将采集到的数据  key value 化
     */
    private JSONObject getData(Annotation[] parameterAnnotations, Object[] args) {
        JSONObject data = new JSONObject();
        if (parameterAnnotations == null || parameterAnnotations.length <= 0) {
            return null;
        }
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation parameterAnnotation = parameterAnnotations[i];
            if (parameterAnnotation instanceof PermasAnnotation) {
                String paramName = ((PermasAnnotation) parameterAnnotation).value();
                data.put(paramName, args[i].toString());
            }
        }
        return data;
    }

    /**
     * 获取到方法参数的注解
     */
    public Annotation[] getMethodParameterNamesByAnnotation(Method method) {
        //获取到放发接收参数的注解 以及 注解上面的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        Annotation[] annotations = new Annotation[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                annotations[i++] = annotation;
            }
        }
        return annotations;
    }

}
