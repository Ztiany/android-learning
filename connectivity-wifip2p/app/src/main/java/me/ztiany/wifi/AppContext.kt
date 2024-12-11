package me.ztiany.wifi

import android.app.Application
import me.ztiany.wifi.kit.arch.FragmentConfig
import me.ztiany.wifi.kit.arch.LoadingViewHost
import me.ztiany.wifi.ui.dialog.AppLoadingViewHost
import me.ztiany.wifip2p.R
import timber.log.Timber

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        FragmentConfig.setDefaultContainerId(R.id.main)
        LoadingViewHost.internalLoadingViewHostFactory = { AppLoadingViewHost(it) }
        Timber.plant(Timber.DebugTree())
    }

}