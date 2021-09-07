package com.ztiany.kotlin

import android.content.Context
import androidx.multidex.MultiDexApplication

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 18:51
 */
class AppContext : MultiDexApplication() {

    private object C {
        lateinit var instance: AppContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        C.instance = this
    }

    companion object {
        fun getInstance(): AppContext {
            return C.instance
        }
    }

}