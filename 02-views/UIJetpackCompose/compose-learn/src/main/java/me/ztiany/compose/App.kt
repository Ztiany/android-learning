package me.ztiany.compose

import android.app.Application
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.extension.init
import com.android.sdk.net.extension.setDefaultHostConfig
import dagger.hilt.android.HiltAndroidApp
import me.ztiany.compose.facility.data.newErrorMessage
import me.ztiany.compose.facility.data.newHttpConfig
import me.ztiany.compose.facility.utils.NetworkUtils
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        configRefreshStateBox()

        NetContext.get().init(this) {
            errorMessage(newErrorMessage())
            platformInteractor(object : PlatformInteractor {
                override fun isConnected(): Boolean {
                    return NetworkUtils.isConnected(this@App)
                }
            })
        }.setDefaultHostConfig {
            httpConfig(newHttpConfig())
            aipHandler { result, hostFlag -> Timber.d("ApiHandler result: $result, hostFlag = $hostFlag") }
        }
    }

}