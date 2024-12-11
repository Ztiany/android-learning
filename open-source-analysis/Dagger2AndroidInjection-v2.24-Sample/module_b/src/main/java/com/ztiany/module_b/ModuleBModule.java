package com.ztiany.module_b;

import com.ztiany.base.di.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 15:38
 */
@Module
abstract class ModuleBModule {

    @FragmentScope
    @ContributesAndroidInjector()
    abstract ListFragment bindListFragment();

}
