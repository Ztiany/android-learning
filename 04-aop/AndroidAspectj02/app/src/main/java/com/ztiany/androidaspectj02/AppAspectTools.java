package com.ztiany.androidaspectj02;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AppAspectTools {

    private static final String TAG = AppAspectTools.class.getSimpleName();

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Log.d(TAG, "----------------" + signature.toString());
    }

}
