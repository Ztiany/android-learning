package com.ztiany.main.main;

import android.util.Log;

import javax.inject.Inject;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-10-12 10:52
 */
class MapValue {

    private static final String TAG = MapValue.class.getSimpleName();

    @Inject
    public MapValue() {
        Log.d(TAG, "MapValue() called");
    }

}
