package com.ztiany.kotlin

import android.app.Application
import android.content.Context

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 18:51
 */
class AppContext : Application() {

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