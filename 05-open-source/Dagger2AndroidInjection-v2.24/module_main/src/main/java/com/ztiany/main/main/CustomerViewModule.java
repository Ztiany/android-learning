package com.ztiany.main.main;

import com.ztiany.base.di.ViewScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 13:32
 */
@Module
public class CustomerViewModule {

    @ViewScope
    @Provides
    String provideMessage(CustomTextView customTextView) {
        return customTextView.getClass().getName() + " I am CustomTextView";
    }

}
