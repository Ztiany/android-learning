package com.ztiany.base;


import com.ztiany.main.MainBuildersModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                BaseModule.class,
                AndroidInjectionModule.class,
                AndroidSupportInjectionModule.class,
                MainBuildersModule.class
        }
)
interface AppComponent {

    HomeAppContext inject(HomeAppContext application);

}