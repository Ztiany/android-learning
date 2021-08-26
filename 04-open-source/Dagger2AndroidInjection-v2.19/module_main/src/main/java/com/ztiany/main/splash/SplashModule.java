package com.ztiany.main.splash;

import dagger.Binds;
import dagger.Module;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-17 18:42
 */
@Module
public abstract class SplashModule {

    /*
     *SplashModule的方法中，可以使用直接使用被注入对象(注入方式不是构造函数注入)作为注入资源，比如此处的SplashActivity
     */
    @Binds
    abstract SplashView splashView(SplashActivity splashActivity);

}
