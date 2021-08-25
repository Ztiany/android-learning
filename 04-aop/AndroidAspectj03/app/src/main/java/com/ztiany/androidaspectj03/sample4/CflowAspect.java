package com.ztiany.androidaspectj03.sample4;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
public class CflowAspect {

    private static final String TAG = CflowAspect.class.getSimpleName();

    @Pointcut("execution(* com.ztiany.androidaspectj03.sample4.CflowTarget.bar(..))")
    public void barPoint() {

    }

    @Pointcut("execution(* com.ztiany.androidaspectj03.sample4.CflowTarget.foo(..))")
    public void fooPoint() {

    }

    @Pointcut("cflow(barPoint())")
    public void barCflow() {

    }

    //获取bar流程内的foo方法调用
    @Pointcut("barCflow() && fooPoint()")
    public void fooInBar() {

    }

    @After("fooInBar()")
    public void beforeFooInBar(JoinPoint joinPoint) {
        Log.d(TAG, "joinPoint.getSignature():" + joinPoint.getSignature());
        Log.d(TAG, "joinPoint.getSourceLocation():" + joinPoint.getSourceLocation());
        Log.d(TAG, "joinPoint.getThis():" + joinPoint.getThis());
        Log.d(TAG, "joinPoint.getArgs():" + Arrays.toString(joinPoint.getArgs()));
        Log.d(TAG, "joinPoint.getKind():" + joinPoint.getKind());
        Log.d(TAG, "joinPoint.getStaticPart():" + joinPoint.getStaticPart());
        Log.d(TAG, "joinPoint.getTarget():" + joinPoint.getTarget());
    }

}