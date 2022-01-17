package com.ztiany.androidnew.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-13 17:20
 */
@Aspect
public class AnalyticsEventAspect {

    private static final String TAG = AnalyticsEventAspect.class.getSimpleName();

    @After("execution(@com.ztiany.androidnew.aop.StatisticEvent * *(..)) && @annotation(eventAnnotation)")
    public void onAnalyticsEvent(JoinPoint joinPoint, StatisticEvent eventAnnotation) throws Throwable {
        String event = eventAnnotation.value();
        Log.d(TAG, "aop 统计事件，event = " + event);
    }

    @After("execution(* onCreate**(..))")
    public void onCreateEvent(JoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "aop onCreateEvent，event = " + joinPoint.getSignature().getName() + " " + joinPoint.getThis().getClass());
    }

}
