package me.ztiany.apm

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Debug
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.text.format.Formatter
import android.view.WindowManager
import me.ztiany.apm.App.Companion.get
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 开启严苛模式
 */
fun startStrictMode() {
    StrictMode.setVmPolicy(
        VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
    )
    StrictMode.setThreadPolicy(
        ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeathOnNetwork()
            .build()
    )
}

internal val dateFormat = object : ThreadLocal<SimpleDateFormat>() {
    override fun initialValue(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
}


@SuppressLint("ObsoleteSdkInt")
internal fun dumpSystemInfo() {
    val time = dateFormat.get()?.format(Date(System.currentTimeMillis())) ?: "unknown time"

    with(StringBuilder()) {
        append("______________  System  ").append(time).append(" ______________")
        appendLine().append("ID                 : ").append(Build.ID)
        appendLine().append("BRAND              : ").append(Build.BRAND)
        appendLine().append("MODEL              : ").append(Build.MODEL)
        appendLine().append("RELEASE            : ").append(Build.VERSION.RELEASE)
        appendLine().append("SDK                : ").append(Build.VERSION.SDK)
        appendLine().append("_______ OTHER _______")
        appendLine().append("BOARD              : ").append(Build.BOARD)
        appendLine().append("PRODUCT            : ").append(Build.PRODUCT)
        appendLine().append("DEVICE             : ").append(Build.DEVICE)
        appendLine().append("FINGERPRINT        : ").append(Build.FINGERPRINT)
        appendLine().append("HOST               : ").append(Build.HOST)
        appendLine().append("TAGS               : ").append(Build.TAGS)
        appendLine().append("TYPE               : ").append(Build.TYPE)
        appendLine().append("TIME               : ").append(Build.TIME)
        appendLine().append("INCREMENTAL        : ").append(Build.VERSION.INCREMENTAL)


        appendLine().append("_______ CUPCAKE-3 _______")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            appendLine().append("DISPLAY            : ").append(Build.DISPLAY)
        }


        appendLine().append("_______ DONUT-4 _______")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            appendLine().append("SDK_INT            : ").append(Build.VERSION.SDK_INT)
            appendLine().append("MANUFACTURER       : ").append(Build.MANUFACTURER)
            appendLine().append("BOOTLOADER         : ").append(Build.BOOTLOADER)
            appendLine().append("CPU_ABI            : ").append(Build.CPU_ABI)
            appendLine().append("CPU_ABI2           : ").append(Build.CPU_ABI2)
            appendLine().append("HARDWARE           : ").append(Build.HARDWARE)
            appendLine().append("UNKNOWN            : ").append(Build.UNKNOWN)
            appendLine().append("CODENAME           : ").append(Build.VERSION.CODENAME)
        }


        appendLine().append("_______ MEMORY-INFO _______")
        val runtime = Runtime.getRuntime()
        val activityManager = get().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val nativeMemoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(nativeMemoryInfo)


        // 表示内核能够访问的总内存量。这基本上等同于设备的 RAM 大小，不包括内核以下固定分配的内存，如直接内存访问 (DMA) 缓冲区、基带 CPU 的 RAM 等。totalMem 提供了设备 RAM 的总量，有助于理解设备的硬件资源。开发者可以根据这个信息优化应用的内存使用，特别是在内存要求较高的应用中。
        appendLine().append("TotalMem           : ").append(Formatter.formatFileSize(get(), memoryInfo.totalMem))
        // 表示系统当前可用的内存量，availMem 可用于了解当前系统剩余的可用内存。这个值对于监测系统的内存状况和决定是否需要释放资源很有用。如果 availMem 很低，应用可能需要减少内存使用，避免系统进行更激进的内存回收措施。
        appendLine().append("AvailMem           : ").append(Formatter.formatFileSize(get(), memoryInfo.availMem))
        // The threshold of availMem at which we consider memory to be low and start killing background services and other non-essential processes.
        appendLine().append("Threshold          : ").append(Formatter.formatFileSize(get(), memoryInfo.threshold))
        // Whether the system is running low on memory.
        appendLine().append("lowMemory          : ").append(memoryInfo.lowMemory)
        // Return the approximate per-application memory class of the current device.
        appendLine().append("memoryClass        : ").append("${activityManager.memoryClass} MB")
        // 这是 Java 虚拟机可能从操作系统申请的堆内存的最大限制。这个值受到 Java 启动时设置的 -Xmx 参数的限制。
        appendLine().append("MaxMemory          : ").append(Formatter.formatFileSize(get(), runtime.maxMemory()))
        // 这是 Java 虚拟机当前从操作系统申请并分配给 Java 堆的内存总量。这部分内存已经是 JVM 的一部分，可以被 JVM 中的对象使用
        appendLine().append("TotalMemory        : ").append(Formatter.formatFileSize(get(), runtime.totalMemory()))
        // freeMemory 部分内存指的是在 JVM 的堆中，已经分配给 JVM 但当前没有被任何 Java 对象使用的内存。这部分内存是随时可用于新对象分配的。
        appendLine().append("FreeMemory         : ").append(Formatter.formatFileSize(get(), runtime.freeMemory()))
        appendLine().append("UsedMemory         : ").append(Formatter.formatFileSize(get(), runtime.totalMemory() - runtime.freeMemory()))
        appendLine().append("UsedPercent        : ").append((runtime.totalMemory() - runtime.freeMemory()) * 1.0F / runtime.maxMemory() * 100)


        // 获取已经申请的 Native 内存
        appendLine().append("NativeHeapSize     : ").append(Formatter.formatFileSize(get(), Debug.getNativeHeapSize()))
        //获取申请但未使用 Native 内存
        appendLine().append("NativeHeapFreeSize : ").append(Formatter.formatFileSize(get(), Debug.getNativeHeapFreeSize()))
        appendLine().append("NativePrivateDirty : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.nativePrivateDirty * 1024L))
        appendLine().append("NativePss          : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.nativePss * 1024L))
        appendLine().append("DalvikPss          : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.dalvikPss * 1024L))
        appendLine().append("DalvikPrivateDirty : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.dalvikPrivateDirty * 1024L))
        appendLine().append("DalvikSharedDirty  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.dalvikSharedDirty * 1024L))
        appendLine().append("NativeSharedDirty  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.nativeSharedDirty * 1024L))
        appendLine().append("OtherPrivateDirty  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.otherPrivateDirty * 1024L))
        appendLine().append("OtherPss           : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.otherPss * 1024L))
        appendLine().append("OtherSharedDirty   : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.otherSharedDirty * 1024L))
        appendLine().append("TotalPrivateClean  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalPrivateClean * 1024L))
        appendLine().append("TotalPrivateDirty  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalPrivateDirty * 1024L))
        appendLine().append("TotalPss           : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalPss * 1024L))
        appendLine().append("TotalSharedClean   : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalSharedClean * 1024L))
        appendLine().append("TotalSharedDirty   : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalSharedDirty * 1024L))
        appendLine().append("TotalSwappablePss  : ").append(Formatter.formatFileSize(get(), nativeMemoryInfo.totalSwappablePss * 1024L))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appendLine().append("_______ MEMORY-STATS _______")
            val memoryStats = nativeMemoryInfo.memoryStats
            for (memoryStat in memoryStats) {
                appendLine().append(memoryStat.key).append(" : ").append(Formatter.formatFileSize(get(), memoryStat.value.toInt() * 1024L))
            }
        }


        appendLine().append("_______ SCREEN-INFO _______");
        appendLine().append("ScreenDensity      : ").append(Resources.getSystem().displayMetrics.density);
        appendLine().append("ScreenWidth        : ").append(getScreenWidth())
        appendLine().append("ScreenHeight       : ").append(getScreenHeight())
        appendLine().append("AppScreenWidth     : ").append(getAppScreenWidth())
        appendLine().append("AppScreenHeight    : ").append(getAppScreenHeight())

        appendLine().append("_______ SCENE _______");
        appendLine().append("CurrentActivity    : ").append(getTopActivity())


        Timber.tag("DEVICES").i(toString())
    }
}

private fun getTopActivity(): String {
    // TODO: 2021/8/3 获取当前的 Activity
    return ""
}


internal fun getScreenWidth(): Int {
    val wm = App.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        wm.defaultDisplay.getRealSize(point)
    } else {
        wm.defaultDisplay.getSize(point)
    }
    return point.x
}


internal fun getScreenHeight(): Int {
    val wm = App.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        wm.defaultDisplay.getRealSize(point)
    } else {
        wm.defaultDisplay.getSize(point)
    }
    return point.y
}

internal fun getAppScreenWidth(): Int {
    val wm = App.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return -1
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.x
}

internal fun getAppScreenHeight(): Int {
    val wm = App.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.y
}