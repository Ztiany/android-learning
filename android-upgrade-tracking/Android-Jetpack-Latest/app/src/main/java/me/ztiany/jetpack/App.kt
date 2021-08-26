package me.ztiany.jetpack

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex

const val TAG = "AndroidLibs"

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-02 11:00
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}