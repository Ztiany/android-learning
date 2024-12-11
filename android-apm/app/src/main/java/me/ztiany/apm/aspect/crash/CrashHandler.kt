package me.ztiany.apm.aspect.crash

import android.app.Application
import android.content.Context
import android.os.Process
import me.ztiany.apm.utils.dumpSystemInfo
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

typealias CrashProcessor = (thread: Thread, ex: Throwable) -> Boolean

/**
 * 通过注册 CrashProcessor 可以监控所有的异常，包括未捕获的异常，因此可以利用它来监控 OOM 的次数和发生 OOM 时后的内存使用情况。
 */
internal class CrashHandler(private val app: Application) {

    private val handlers = ConcurrentHashMap<Class<out Throwable>, CrashProcessor>()

    fun registerCrashProcessor(clazz: Class<out Throwable>, crashProcessor: CrashProcessor) {
        handlers[clazz] = crashProcessor
    }

    fun install() {
        Thread.currentThread().uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
            dispatchOrHandleCrash(app, t, e)
        }
    }

    private fun dispatchOrHandleCrash(app: Application, thread: Thread, throwable: Throwable) {
        Timber.e(throwable, "dispatchOrHandleCrash")
        // 先让注册的处理器处理
        handlers[throwable::class.java]?.let {
            if (it(thread, throwable)) {
                return
            }
        }

        // 收集异常信息
        restoreCrash(app, thread, throwable)
        // 退出
        Process.killProcess(Process.myPid())
    }

    private fun restoreCrash(context: Context, thread: Thread, ex: Throwable) {
        ex.printStackTrace(System.err)
        /*
         * 通过这些数据与所处页面的结合，我们可以得出各个页面的内存使用情况，从而可以在应用发生内存不足的崩溃后，分析出从什么模块开始，内存被大量使用；在哪个模块，内存被完全耗尽，从而缩小问题范围。
         *
         * 下面方法是打印各种系统信息，包括内存信息、线程信息、进程信息、系统信息等。在实际生产中，可以将信息存储到文件中，之后上报到服务器。
         */
        dumpSystemInfo()
    }

}