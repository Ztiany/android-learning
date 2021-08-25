package com.ztiany.main.main;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface CustomTextViewComponent extends AndroidInjector<CustomTextView> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<CustomTextView> {

    }

}