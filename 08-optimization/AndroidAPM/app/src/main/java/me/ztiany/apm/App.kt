package me.ztiany.apm

import android.app.Application
import android.content.Context
import me.ztiany.apm.aspect.crash.CrashHandler
import me.ztiany.apm.aspect.gc.GCMonitor
import me.ztiany.apm.aspect.memory.LMKMonitor
import me.ztiany.apm.aspect.memory.MemoryClassification
import timber.log.Timber
import kotlin.properties.Delegates

class App : Application() {

    override fun onCreate() {
        context = this
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        crashHandler.install(this)
        lmkMonitor.install(this)
        memoryClassification.install(this)
        gcMonitor.install(this)
    }

    companion object {

        private var context: Context by Delegates.notNull()

        @JvmStatic
        fun get(): Context {
            return context
        }

        internal val crashHandler by lazy { CrashHandler() }
        internal val jniBridge by lazy { JNIBridge() }
        internal val lmkMonitor by lazy { LMKMonitor() }
        internal val memoryClassification by lazy { MemoryClassification() }
        internal val gcMonitor by lazy { GCMonitor() }
    }

}