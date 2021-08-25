package me.ztiany.android.database

import android.app.Application

class App : Application() {

    companion object {
        private lateinit var app: App

        @JvmStatic
        fun getApp() = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }


}