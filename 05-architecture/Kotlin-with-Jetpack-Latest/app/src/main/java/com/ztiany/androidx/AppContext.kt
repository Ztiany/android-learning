package com.ztiany.androidx

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 18:51
 */
@HiltAndroidApp
class AppContext : MultiDexApplication() {

    private object C {
        lateinit var instance: AppContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        C.instance = this
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    companion object {
        fun getInstance(): AppContext {
            return C.instance
        }
    }

}