package com.ztiany.base.di;

import android.view.View;

import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.internal.Beta;

import static java.lang.annotation.ElementType.METHOD;

@Beta
@MapKey
@Target(METHOD)
public @interface ViewKey {
    Class<? extends View> value();
}
