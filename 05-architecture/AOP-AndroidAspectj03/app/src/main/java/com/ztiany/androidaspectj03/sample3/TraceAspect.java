/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.ztiany.androidaspectj03.sample3;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect representing the cross cutting-concern: Method and Constructor Tracing.
 */
@Aspect
public class TraceAspect {


    @Pointcut("within(@com.ztiany.androidaspectj03.sample3.DebugTrace *)")
    public void withinAnnotatedClass() {}

    //有synthetic标记的field和method是class内部使用的，正常的源代码里不会出现synthetic field。这里用来作为排除class内部方法的切入点
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    @Pointcut("execution(@com.ztiany.androidaspectj03.sample3.DebugTrace * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    @Pointcut("execution(@com.ztiany.androidaspectj03.sample3.DebugTrace *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

    @Around("method() || constructor()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        Log.d(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));
        return result;
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        return "TraceAspect --> " +
                methodName +
                " --> " +
                "[" +
                methodDuration +
                "ms" +
                "]";
    }
}
