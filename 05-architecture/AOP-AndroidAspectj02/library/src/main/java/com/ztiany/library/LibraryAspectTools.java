package com.ztiany.library;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LibraryAspectTools {

    private static final String TAG = LibraryAspectTools.class.getSimpleName();

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "拦截方法-------------------------" + joinPoint);
    }

}
