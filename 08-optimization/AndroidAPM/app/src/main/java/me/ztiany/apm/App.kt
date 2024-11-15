package me.ztiany.apm

import android.app.Application
import android.content.Context
import me.ztiany.apm.aspect.bitmap.BigBitmapMonitor
import me.ztiany.apm.aspect.crash.CrashHandler
import me.ztiany.apm.aspect.gc.GCMonitor
import me.ztiany.apm.aspect.memory.LMKMonitor
import timber.log.Timber
import kotlin.properties.Delegates

class App : Application() {

    override fun onCreate() {
        context = this
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        crashHandler.install()
        lmkMonitor.install()
        gcMonitor.install()
        bigBitmapMonitor.install()
    }

    companion object {

        private var context: Application by Delegates.notNull()

        @JvmStatic
        fun get(): Context {
            return context
        }

        internal val crashHandler by lazy { CrashHandler(context) }
        internal val lmkMonitor by lazy { LMKMonitor(context) }
        internal val gcMonitor by lazy { GCMonitor(context) }
        internal val bigBitmapMonitor by lazy { BigBitmapMonitor(context) }
        internal val jniBridge by lazy { JNIBridge() }
    }

}