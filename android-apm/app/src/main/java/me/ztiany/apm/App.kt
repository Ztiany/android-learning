package me.ztiany.apm

import android.app.Application
import android.content.Context
import me.ztiany.apm.aspect.bitmap.BitmapMonitor
import me.ztiany.apm.aspect.bitmap.MonitorConfig
import me.ztiany.apm.aspect.crash.CrashHandler
import me.ztiany.apm.aspect.fluency.FPSMonitor
import me.ztiany.apm.aspect.memory.GCMonitor
import me.ztiany.apm.aspect.memory.LMKMonitor
import me.ztiany.apm.aspect.memory.MemoryTracker
import me.ztiany.apm.utils.ActivityThreadHook
import me.ztiany.apm.utils.doBusyWork
import timber.log.Timber
import kotlin.properties.Delegates

class App : Application() {

    init {
        doBusyWork()
        doBusyWork()
        doBusyWork()
        doBusyWork()
        doBusyWork()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        doBusyWork()
        doBusyWork()
        doBusyWork()
        doBusyWork()
    }

    override fun onCreate() {
        context = this
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ActivityThreadHook.getInstance().hookActivityThread()

        crashHandler.install()
        lmkMonitor.install()
        gcMonitor.install()
        gcMonitor.install()
        fpsMonitor.install()
        bitmapMonitor.install(
            MonitorConfig(
                imageStorageDirectory = getExternalFilesDir("bitmap_monitor")?.absolutePath
                    ?: throw IllegalStateException("getExternalFilesDir failed")
            )
        )

        doBusyWork()
        doBusyWork()
    }

    companion object {

        private var context: App by Delegates.notNull()

        @JvmStatic
        fun get(): Context {
            return context
        }

        internal val crashHandler by lazy { CrashHandler(context) }
        internal val lmkMonitor by lazy { LMKMonitor(context) }
        internal val gcMonitor by lazy { GCMonitor(context) }
        internal val bitmapMonitor by lazy { BitmapMonitor(context) }
        internal val fpsMonitor by lazy { FPSMonitor(context) }
        internal val memoryTracker by lazy { MemoryTracker() }
    }

}