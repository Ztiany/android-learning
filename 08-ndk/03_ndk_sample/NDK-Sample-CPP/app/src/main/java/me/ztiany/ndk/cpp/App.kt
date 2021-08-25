package me.ztiany.ndk.cpp

import android.app.Application

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-21 17:08
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("native-lib")
    }

}