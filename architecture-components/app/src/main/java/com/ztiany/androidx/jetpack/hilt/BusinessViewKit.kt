package com.ztiany.androidx.jetpack.hilt

import android.util.Log
import javax.inject.Inject

private const val TAG = "BusinessViewKit"

class BusinessViewKit @Inject constructor() {

    fun doBusiness() {
        Log.d(TAG, "doBusiness: $this")
    }

}