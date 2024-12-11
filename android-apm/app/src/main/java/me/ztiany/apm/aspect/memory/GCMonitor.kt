package me.ztiany.apm.aspect.memory

import android.app.Application
import android.os.Build
import android.os.Debug
import androidx.annotation.RequiresApi
import timber.log.Timber

/**
 * GC 可以概括为两种类型：阻塞式和非阻塞式。阻塞式 GC 是指在进行 GC 时，会阻塞 GC 发起线程；而非阻塞 GC 是指并发进行 GC，不会显示阻塞其他线程。
 */
internal class GCMonitor(private val app: Application) {

    fun install() {
        monitorGC()
        monitorHeapTaskDaemon()
    }

    private fun monitorGC() {
        // 我们可以通过 `Debug.getRuntimeStat` 获取到应用当前的 GC 次数和耗时：
        //      blockGcCount，blockGcTime 是应用从启动到查询时阻塞式 gc 的次数和时长；
        //      gcCount，gcTime 就是应用从启动到查询时的非阻塞次数和时长。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val gcCount: Long = getGcInfoSafely("art.gc.gc-count")
            val gcTime: Long = getGcInfoSafely("art.gc.gc-time")
            val blockGcCount: Long = getGcInfoSafely("art.gc.blocking-gc-count")
            val blockGcTime: Long = getGcInfoSafely("art.gc.blocking-gc-time")
            Timber.d("gcCount: $gcCount, gcTime: $gcTime, blockGcCount: $blockGcCount, blockGcTime: $blockGcTime")
        } else {
            Timber.w("VERSION.SDK_INT < M")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getGcInfoSafely(info: String): Long {
        return try {
            Debug.getRuntimeStat(info).toLong()
        } catch (throwable: Throwable) {
            Timber.e(throwable, "getGcInfoSafely")
            -1
        }
    }

    /**
     * 除了 GC 次数，并发执行的垃圾回收线程 HeapTaskDaemon 的繁忙程度也可以用于衡量 GC 的影响。如果在启动、页面加载等核心场景，HeapTaskDaemon 线程的 CPU 使用比主线程还高，
     * 那就说明，GC 对应用的性能有严重影响。所以我们需要追踪 HeapTaskDaemon 的 CPU 时长。在运行时，我们可以通过遍历进程的 `/proc/{pid}/task/`，找到名称为 HeapTaskDaemon
     * 的 tid，然后从 `/proc/{pid}/task/{tid}/stat` 中读取 CPU time 相关数据。
     *
     * 然后从数据中找到这个线程的用户态时长和内核态时长，把它们累加起来，就是线程从创建到查询时的 CPU 使用时长。通过与主线程的 CPU 时长对比，我们可以判断出 HeapTaskDaemon 是否
     * 执行太频繁，从而决定是否需要对其进行优先级降低和抑制。
     */
    private fun monitorHeapTaskDaemon() {

    }

    fun dump() {
        monitorGC()
        monitorHeapTaskDaemon()
    }

}