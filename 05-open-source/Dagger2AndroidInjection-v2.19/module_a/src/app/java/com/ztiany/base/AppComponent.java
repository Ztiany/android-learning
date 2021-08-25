package com.ztiany.base;

import com.ztiany.module_a.ModuleABuildersModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:18
 */
@Component(
        modules = {
                BaseModule.class,
                AndroidInjectionModule.class,
                AndroidSupportInjectionModule.class,
                ModuleABuildersModule.class
        }
)
@Singleton
interface AppComponent {
        void inject(AAppContext aAppContext);
}
