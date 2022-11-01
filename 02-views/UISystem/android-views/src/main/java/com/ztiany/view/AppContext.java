package com.ztiany.view;

import android.app.Application;

import com.ztiany.view.utils.BaseUtils;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-10-16 22:59
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);
    }

}
