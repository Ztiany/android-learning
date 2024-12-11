package me.ztiany.apm.aspect.bitmap

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.bytedance.shadowhook.ShadowHook
import me.ztiany.apm.BuildConfig
import timber.log.Timber
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class BitmapMonitor(private val context: Context) {

    private lateinit var config: MonitorConfig

    private var currentScene: String = "Unknown"

    private val installed = AtomicBoolean(false)

    fun install(config: MonitorConfig): Boolean {
        return if (installed.compareAndSet(false, true)) {
            this.config = config
            Timber.d("BitmapMonitor install with config: $config")
            with(File(config.imageStorageDirectory)) {
                deleteRecursively()
                mkdirs()
            }
            installSceneProvider()
            doRealInstall()
        } else {
            Timber.w("BitmapMonitor has been installed.")
            false
        }
    }

    private fun installSceneProvider() {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit

            override fun onActivityResumed(activity: Activity) {
                currentScene = activity.javaClass.name
            }

        })
    }

    private fun doRealInstall(): Boolean {
        try {
            System.loadLibrary("bitmap_monitor")
        } catch (t: Throwable) {
            Timber.e(t, "loadLibrary bitmap_monitor failed")
            return false
        }

        val ret = ShadowHook.init()
        ShadowHook.setDebuggable(BuildConfig.DEBUG)
        Timber.w("ShadowHook init result: $ret")
        return ret == 0
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun start() {
        if (installed.get()) {
            hookBitmapCreationNative(
                config.checkRecycleInterval,
                config.obtainStackTraceThreshold,
                config.dumpToFileThreshold,
                config.imageStorageDirectory
            )
        } else {
            Timber.e("BitmapMonitor has not been installed.")
        }
    }

    fun dumpBitmapStatistics(): BitmapStatistics? {
        return if (installed.get()) {
            dumpBitmapStatisticsNative()
        } else {
            Timber.e("BitmapMonitor has not been installed.")
            null
        }
    }

    fun dumpBitmapSimpleStatistics(): BitmapStatistics? {
        return if (installed.get()) {
            dumpBitmapSimpleStatisticsNative()
        } else {
            Timber.e("BitmapMonitor has not been installed.")
            null
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Called by native
    ///////////////////////////////////////////////////////////////////////////

    private fun reportBitmapStatisticsByNative(bitmapStatistics: BitmapStatistics) {
        Timber.d("reportBitmapStatistics: $bitmapStatistics")
    }

    private fun reportLargeBitmapStoredByNative(storePath: String) {
        Timber.d("reportLargeBitmapStoredByNative: $storePath")
    }

    private fun dumpJavaStackTraceByNative(): String {
        val st = Thread.currentThread().stackTrace
        return st.joinToString("\n")
    }

    private fun getCurrentSceneByNative(): String {
        return currentScene
    }

    ///////////////////////////////////////////////////////////////////////////
    // Native
    ///////////////////////////////////////////////////////////////////////////

    private external fun hookBitmapCreationNative(
        checkRecycleInterval: Long,
        obtainStackThreshold: Long,
        dumpToFileThreshold: Long,
        storageDir: String,
    ): Int

    private external fun releaseBitmapCreationNative()

    private external fun dumpBitmapSimpleStatisticsNative(): BitmapStatistics?

    private external fun dumpBitmapStatisticsNative(): BitmapStatistics?

}