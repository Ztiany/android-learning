package com.ztiany.base.di;

import android.view.View;

import dagger.MembersInjector;

/**
 * Created by froger_mcs on 16/10/2016.
 */
public interface ViewComponent<A extends View> extends MembersInjector<A> {

}
