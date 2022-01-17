package com.ztiany.transform.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectTools {

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) {
        Object aThis = joinPoint.getThis();
        Signature signature = joinPoint.getSignature();
        Log.d(aThis.getClass().getSimpleName(), "onActivityMethodBefore: " + signature.toString());
    }

    @Pointcut("execution(@com.ztiany.transform.aspectj.DebugTools * *(..))")
    public void debugTools() {
    }

    @Before("debugTools()")
    public void onDebugToolsBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        String TAG = joinPoint.getThis().getClass().getSimpleName();
        Log.d(TAG, "onDebugToolMethodBefore: " + key);
    }

}
