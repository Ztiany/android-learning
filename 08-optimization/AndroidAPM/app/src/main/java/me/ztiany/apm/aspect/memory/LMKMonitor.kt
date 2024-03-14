package me.ztiany.apm.aspect.memory

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import timber.log.Timber
import java.io.File


/**
 * 监控被 LMK 杀死的次数。
 */
class LMKMonitor {

    fun install(app: Application) {
        getExistReason(app)
        observeOOMScore(app)
    }

    /**
     * 影响应用的后台存活时间，除了设备环境，还有应用本身的状态，包括 `oom_score` 和优先级。在 LMK 指标有变化时，我们可以通过它们进一步分析，是不是因为某个业务需求影响了应用整体的优先级。
     * 首先我们来看一下如何获取应用的 `oom_score`。它是 LMK 机制对不同进程的评分，根据应用的优先级动态调整。在 LMK 执行进程清理时会根据这个分数决定先清理谁。**`oom_score` 越大，越容易被杀**。
     */
    private fun observeOOMScore(app: Application) {
        /*
         *我们可以通过读取 `/proc/{pid}/oom_score_adj` 来获取应用的 `oom_score`：
         */
        try {
            val scoreAdjPath = "/proc/${Process.myPid()}/oom_score_adj"
            val content: String = File(scoreAdjPath).readText()
            Timber.d("oom_score_adj path: $scoreAdjPath : $content")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*
        经过测试，在部分低版本机型上，应用没有权限读取这个节点的数据。不过不用担心，我们还可以通过 `ActivityManager.getMyMemoryState` 获取到应用的优先级（或重要性，本文统称“优先级”）数据，
        对于 LMK 机制它的概念和 `oom_score` 很接近：
         */
        val processInfo = RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(processInfo)
        //importance 可以用于判断是否前后台，这个值可以结合 oom_score_adj。
        Timber.d("process importance: ${processInfo.importance} , lastTrimLevel: ${processInfo.lastTrimLevel}")
    }


    /**
     * 获取应用被 LMK 强杀的原因。
     */
    private fun getExistReason(app: Application) {
        val systemService = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            /*
                在 Android 11 及以上，系统提供了获取应用上次退出的信息的 API（ActivityManager.getHistoricalProcessExitReasons），通过它可以获取到应用退出的原因、当时进程的优先级和物理内存等信息。
                如果应用被 LMK 强杀过，下次启动，就能查询到这次记录。
             */

            val processExitReasons = systemService.getHistoricalProcessExitReasons(app.packageName, 0, 100)
            // ApplicationExitInfo.REASON_LOW_MEMORY
            processExitReasons.forEach {
                Timber.d("processExitReasons: $it")
            }
        } else {
            // 另外，应用被 LMK 强杀时，也会有对应的 Logcat 日志：
            //          ActivityManager: Killing 3281:top.shixinzhang.example (adj 900):stop top.shixinzhang.example
            // 因此在低于 Android 11 的手机上，在崩溃时上报最近的 Logcat 数据，分析其中是否有 `Killing pid:package (adj xxx)` 等关键字，也可以得到出被强杀的数据。
            // TODO: 2021/8/17 通过读取 Logcat 日志，获取应用被 LMK 强杀的信息。
        }
    }

}