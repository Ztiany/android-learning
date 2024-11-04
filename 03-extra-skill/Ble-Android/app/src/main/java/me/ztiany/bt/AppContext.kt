package me.ztiany.bt

import android.app.Application
import me.ztiany.bt.kit.arch.FragmentConfig
import me.ztiany.bt.kit.arch.LoadingViewHost
import me.ztiany.bt.ui.dialog.AppLoadingViewHost
import me.ztiany.ble.R
import timber.log.Timber

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        FragmentConfig.setDefaultContainerId(R.id.main)
        LoadingViewHost.internalLoadingViewHostFactory = { AppLoadingViewHost(it) }
        Timber.plant(Timber.DebugTree())
    }

}