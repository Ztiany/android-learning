package com.ztiany.main.main;

import com.ztiany.base.di.ViewComponent;
import com.ztiany.base.di.ViewComponentBuilder;
import com.ztiany.base.di.ViewScope;

import dagger.Subcomponent;

@ViewScope
@Subcomponent(modules = CustomerViewModule.class)
public interface CustomTextViewComponent extends ViewComponent<CustomTextView> {

    @Subcomponent.Builder
    interface Builder extends ViewComponentBuilder<CustomTextView, CustomTextViewComponent> {
    }

}