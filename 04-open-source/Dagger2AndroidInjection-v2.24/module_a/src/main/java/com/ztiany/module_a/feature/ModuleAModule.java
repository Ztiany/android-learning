package com.ztiany.module_a.feature;

import dagger.Binds;
import dagger.Module;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:41
 */
@Module
@SuppressWarnings("all")
public abstract class ModuleAModule {

    @Binds
    abstract ModuleAView provideModuleA_1View(ModuleAActivity a_1Activity);
}
