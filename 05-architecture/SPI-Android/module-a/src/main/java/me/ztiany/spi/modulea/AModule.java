package me.ztiany.spi.modulea;

import android.util.Log;

import me.ztiany.spi.base.IModule;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-12-14 21:30
 */
public class AModule implements IModule {

    private static final String TAG = "AModule";

    @Override
    public void init() {
        Log.d(TAG, "init() called");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy() called");
    }

}
