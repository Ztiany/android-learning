package com.ztiany.view;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

import com.ztiany.view.utils.BaseUtils;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 22:59
 */
public class AppContext extends Application{

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);
    }

}
