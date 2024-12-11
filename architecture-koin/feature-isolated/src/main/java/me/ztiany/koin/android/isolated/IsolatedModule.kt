package me.ztiany.koin.android.isolated

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import java.util.concurrent.atomic.AtomicBoolean

/**
 *@author Ztiany
 */
object IsolatedModule {

    private val initProtection = AtomicBoolean(false)

    private lateinit var koinApp: KoinApplication

    val koin by lazy {
        koinApp.koin
    }

    fun init(appContext: Context) {
        if (initProtection.compareAndSet(false, true)) {
            doRealInit(appContext)
        }
    }

    private fun doRealInit(appContext: Context) {
        koinApp = koinApplication {
            androidLogger(Level.DEBUG)
            androidContext(appContext)
            modules(dataModule, presenterModule)
        }
    }

}