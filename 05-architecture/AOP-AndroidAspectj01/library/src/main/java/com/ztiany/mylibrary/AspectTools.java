package com.ztiany.mylibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect//@Aspect注解来定义这是一个AspectJ文件
public class AspectTools {

    /*
### Join Points(连接点)

     简称JPoints，是AspectJ的核心思想之一，它就像一把刀，把程序的整个执行过程切成了一段段不同的部分。
     JoinPoint可以是：构造方法调用、调用方法、方法执行、异常等等。实际上就是想把新的代码插在程序的那个地方。
     如下面onActivityMethodBefore分发的参数，就是一个JoinPoint。

### Pointcuts(切入点)

     在Join Points中通过一定条件选择出所需要的Join Points

###Advice(具体插入的代码)

     - Before：在插入点前插入代码
     - After：在插入点后插入代码
     - Around：在插入点前后插入代码
     - AfterThrowing
     - AfterReturning

Advance为Before和After时，切入方法的参数应该JoinPoint，而Advance为Around时，方法参数应该为ProceedingJoinPoint，ProceedingJoinPoint继承JoinPoint，多了proceed功能，
此时如果我们不调用proceed方法，被切入的方法将不会被调用，Around和After是不能同时作用在同一个方法上的，会产生重复切入的问题。

*/

    private static final String TAG = AspectTools.class.getSimpleName();

    @Before("execution(* android.app.Activity.on*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        Object aThis = joinPoint.getThis();
        Signature signature = joinPoint.getSignature();
        Log.d(aThis.getClass().getSimpleName(), "onActivityMethodBefore: " + signature.toString());
    }

    @Around("execution(* com.ztiany.androidaspectj.MainActivity.testAopAround(..))")
    public void testMainActivityAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        proceedingJoinPoint.proceed();
        Log.d(proceedingJoinPoint.getThis().getClass().getSimpleName(), "testMainActivityAround() called with: joinPoint = [" + proceedingJoinPoint + "]");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 自定义Pointcuts：
    ///////////////////////////////////////////////////////////////////////////

   /*
   自定义Pointcuts可以让我们更加精确的切入一个或多个指定的切入点。
            1. 定义注解，如DebugTools
            2. 定义Pointcut，并声明要监控的方法名
            3. 在Before或者其它Advice里面添加切入代码，即可完成切入。
     */
    //声明Pointcut，声明debugTools方法
    @Pointcut("execution(@com.ztiany.androidaspectj.aspect.DebugTools * *(..))")
    public void debugTools() {

    }

    @Before("debugTools()")
    public void onDebugToolsBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        String TAG = joinPoint.getThis().getClass().getSimpleName();
        Log.d(TAG, "onDebugToolMethodBefore: " + key);
    }

    //call：call与execution的区别在于，execution是把切入的代码调用放在方法内部，而call则是把切入的代码的调用放在被切入方法的外围
    @Around("call(* com.ztiany.androidaspectj.MainActivity.testAopCall(..))")
    public void testMainActivityAroundCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.d(joinPoint.getThis().getClass().getSimpleName(), "testMainActivityAroundCall() called with: joinPoint = [" + key + "]");
    }

    // withincode：通常来进行一些切入点条件的过滤，作更加精确的切入控制
    // 在testAopWithCode1方法内
    @Pointcut("withincode(* com.ztiany.androidaspectj.MainActivity.testAopWithCode1(..))")
    public void withincodeTestAopWithCode1() {

    }

    // 在调用withCodeRun方法的时候
    @Pointcut("call(* com.ztiany.androidaspectj.MainActivity.withCodeRun(..))")
    public void callWithCodeRun() {

    }

    // 同时满足前面的条件
    @Pointcut("withincodeTestAopWithCode1() && callWithCodeRun()")
    public void invokeOnlyTestAopWithCode1() {

    }

    @Around("invokeOnlyTestAopWithCode1()")
    public void beforeInvokeTestAopWithCode1(ProceedingJoinPoint joinPoint) {
        String key = joinPoint.getSignature().toString();
        String TAG = joinPoint.getThis().getClass().getSimpleName();
        Log.d(TAG, "beforeInvokeTestAopWithCode1() called with: joinPoint = [" + key + "]");
    }

    /*
    AfterThrowing：异常处理
         1. AfterThrowing虽然可以捕获已异常，但是最终还是会把异常抛出，估计只能做一些统计日志和统计功能。
         2. 如果目标分发已经处理了异常，则Aspect不会处理异常
     */
    @AfterThrowing(pointcut = "execution(* com.ztiany.androidaspectj.MainActivity.error1(..))", throwing = "exception")
    public void catchExceptionMethod1(Exception exception) {
        Log.d(TAG, "catchExceptionMethod1() called with: exception = [" + exception + "]");
    }

    @AfterThrowing(pointcut = "execution(* com.ztiany.androidaspectj.MainActivity.error2(..))", throwing = "exception")
    public void catchExceptionMethod2(Exception exception) {
        Log.d(TAG, "catchExceptionMethod2() called with: exception = [" + exception + "]");
    }

}
