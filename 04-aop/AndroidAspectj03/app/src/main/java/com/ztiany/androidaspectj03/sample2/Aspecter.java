package com.ztiany.androidaspectj03.sample2;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * 演示：
 * <pre>
 *     使用Pointcut定义切入点，并且使用该切入点切入点之间的逻辑组合
 *     AfterThrowing
 *     withincode和within
 *     call和execution的区别：call捕获的JoinPoint是签名方法的调用点，而execution捕获的则是执行点
 *     字段获取和设置切入点
 * </pre>
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-25 18:31
 */
@Aspect
public class Aspecter {

    private static final String TAG = Aspecter.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////
    // sample 1
    ///////////////////////////////////////////////////////////////////////////

    @Around("execution(* *..BaseTarget1.*(..)) && !testAroundPoint() && !loginPoint()")
    public void onAspectTarget1MethodRun(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Log.d(TAG, "BaseTarget1方法执行" + signature.toString());
        joinPoint.proceed();
    }

    //执行runAll方法的切入点，此处必须为execution
    @Pointcut("execution(* *..BaseTarget1.testAround(..))")
    public void testAroundPoint() {

    }

    @Pointcut("execution(* *..BaseTarget1.login(..))")//执行login方法的切入点
    public void loginPoint() {

    }

    //https://stackoverflow.com/questions/11270459/aspectj-around-pointcut-all-methods-in-java
    @Around("loginPoint()")
    public Object aroundLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log.d(TAG, "BaseTarget1方法执行，由loginPoint打印：" + proceedingJoinPoint.getSignature().toString());
        Object proceed = proceedingJoinPoint.proceed();
        Log.d(TAG, "loginPoint执行login，执行结果为:" + proceed);
        return proceed;
    }

    ///////////////////////////////////////////////////////////////////////////
    // sample2：AfterThrowing
    ///////////////////////////////////////////////////////////////////////////

    /*
        AfterThrowing：异常处理
                1. AfterThrowing虽然可以捕获已异常，但是最终还是会把异常抛出，估计只能做一些统计日志和统计功能。
                2. 如果目标分发已经处理了异常，则Aspect不会处理异常
    */
    @AfterThrowing(pointcut = "execution(* *..BaseTarget2.throwError(..))", throwing = "exception")
    public void throwErrorPoint(Exception exception) {//这里的参数名必须和上面throwing = "exception"指定的名称一致
        Log.e(TAG, "throwErrorPoint 拦截到错误= [" + exception + "]");
    }


    ///////////////////////////////////////////////////////////////////////////
    //sample3：Pointcut和withincode
    ///////////////////////////////////////////////////////////////////////////

    // withincode：通常来进行一些切入点条件的过滤，作更加精确的切入控制
    // 此处切入点表示：在print1方法中执行的
    @Pointcut("withincode(* *..BaseTarget2.print1())")
    public void withincodePrint1Point() {

    }

    // 在调用doPrint方法的时候
    @Pointcut("call(* *..BaseTarget2.doPrint(..))")
    public void callDoPrintPoint() {

    }

    // 条件限定为：在print1方法中调用doPrint的时候
    @Pointcut("withincodePrint1Point() && callDoPrintPoint()")
    public void inPrint1AndCallDoPrintPoint() {

    }

    @Around("inPrint1AndCallDoPrintPoint()")
    public void aroundPrint(ProceedingJoinPoint joinPoint) {
        Log.d(TAG, "拦截到" + joinPoint.getThis().getClass().getSimpleName() + "的" + joinPoint.getSignature().getName() + "方法，不执行它");
    }

    ///////////////////////////////////////////////////////////////////////////
    // sample 4：所有的withInSample方法，并且该方法在BaseTarget1中
    ///////////////////////////////////////////////////////////////////////////
    @Around("execution(* withInSample(..)) && within(com.ztiany.androidaspectj03.sample2.BaseTarget1)")
    public void aroundTestWithIn(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Object aThis = joinPoint.getThis();
        Log.d(TAG, "拦截到方法，签名是：" + signature.toString() + " , 方法属于:" + aThis.getClass().getSimpleName());
    }

    ///////////////////////////////////////////////////////////////////////////
    // sample 5 异常处理
    ///////////////////////////////////////////////////////////////////////////
    @Before("call(* *..BaseTarget2.diffCallAndExecution(..))")
    public void befroeCallPoint(JoinPoint joinPoint) {
        Log.d(TAG, "拦截调用diffCallAndExecution方法：位置：" + joinPoint.getSourceLocation());
    }

    @Before("execution(* *..BaseTarget2.diffCallAndExecution(..))")
    public void befroeExecutionPoint(JoinPoint joinPoint) {
        Log.d(TAG, "拦截执行diffCallAndExecution方法：位置：" + joinPoint.getSourceLocation());
    }

    ///////////////////////////////////////////////////////////////////////////
    // sample 6 读取和设置字段切面
    ///////////////////////////////////////////////////////////////////////////
    @Pointcut("get(int *..BaseTarget3.age)")
    public void ageGetPoint() {

    }

    @Pointcut("get(String *..BaseTarget3.name)")
    public void nameGetPoint() {

    }

    @Pointcut("set(String *..BaseTarget3.birthday)")
    public void birthdaySetPoint() {

    }

    @Pointcut("set(String *..BaseTarget3.address)")
    public void addressSetPoint() {

    }

    @Around("ageGetPoint()")
    public Object aroundAgeGetPoint(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object proceed = thisJoinPoint.proceed();
        Log.d(TAG, "aroundAgeGetPoint拦截到getAge" + thisJoinPoint.getSignature().getName() + "返回值为：" + proceed);
        return (int) proceed + 10;
    }

    @AfterReturning(pointcut = "nameGetPoint()", returning = "name")
    public void fieldget2(JoinPoint joinPoint, String name) {
        Log.d(TAG, "aroundAgeGetPoint拦截到getName" + joinPoint.getSignature().getName() + "返回值为：" + name);
    }

    @Around("birthdaySetPoint()")
    public void aroundBirthdaySetPoint(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Log.d(TAG, "thisJoinPoint.getArgs():" + Arrays.toString(thisJoinPoint.getArgs()));
        thisJoinPoint.proceed(new String[]{"1990-11-21"});
        Log.d(TAG, "aroundBirthdaySetPoint拦截到setBirthday" + thisJoinPoint.getSignature().getName());
    }
}
