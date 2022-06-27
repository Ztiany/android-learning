package com.ztiany.androidx

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates

/**
 *@author Ztiany
 */
@HiltAndroidApp
class AppContext : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        application = this
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    companion object {

        private var application by Delegates.notNull<Application>()

        fun getInstance(): Application {
            return application
        }

    }

}