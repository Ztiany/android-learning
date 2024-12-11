package com.ztiany.base;

import com.ztiany.main.MainBuildersModule;
import com.ztiany.module_a.ModuleABuildersModule;
import com.ztiany.module_b.ModuleBBuildersModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 10:55
 */
@Component(
        modules = {
                BaseModule.class,
                AndroidInjectionModule.class,
                AndroidSupportInjectionModule.class,
                ModuleABuildersModule.class,
                ModuleBBuildersModule.class,
                MainBuildersModule.class,
        }
)
@Singleton
public interface AppComponent {
        void inject(HomeAppContext homeAppContext);
}
