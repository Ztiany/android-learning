package me.ztiany.apm.aspect.fluency

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import android.view.FrameMetrics
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import me.ztiany.apm.App
import me.ztiany.apm.common.ActivityLifecycleCallbacksAdapter
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max
import kotlin.math.min


/**
 * 评估 App 的流畅度时最常使用的指标之一是 FPS。理论上 FP S越高代表画面切换越连贯，
 * 也就越流畅；FPS 越低说明每秒展示的帧越少，相同画面停留的时间越久，也就越容易让用
 * 户感觉卡顿。
 */
class FPSMonitor(private val context: App) {

    private val installed = AtomicBoolean(false)

    /** 屏幕刷新率 */
    private val refreshRate = getRefreshRate()

    /** 理论上一帧的时间 */
    private val intendedFrameTimeMs = 1_000 / refreshRate

    private var lastFrameTimeNanos = 0L

    private var totalFrameCount = 0
    private var totalDropFrameCount = 0
    private var totalFrameDurationMs = 0F

    private var lastFPSReportTimeMs = 0L

    private var isScrolling = false

    private val uiHandler = Handler(Looper.getMainLooper())

    private val onScrollFinishRunnable = Runnable { isScrolling = false }

    private val callback = object : Choreographer.FrameCallback {

        override fun doFrame(frameTimeNanos: Long) {
            calculateFPS(frameTimeNanos)
            if (installed.get()) {
                Choreographer.getInstance().postFrameCallback(this)
            }
        }
    }

    private fun getRefreshRate(): Float {
        val oldGetter = { (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.refreshRate }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            /*
            Tried to obtain display from a Context not associated with one.
            Only visual Contexts (such as Activity or one created with Context#createWindowContext) or ones created with Context#createDisplayContext
            are associated with displays. Other types of Contexts are typically related to background entities and may return an arbitrary display.
             */
            /*context.display?.refreshRate ?:*/ oldGetter()
        } else {
            oldGetter()
        }
    }

    fun install() {
        if (installed.compareAndSet(false, true)) {
            installListeners()
            doInstall()
        } else {
            Timber.w("FPSMonitor already installed.")
        }
    }

    private fun installListeners() {
        /*
        使用 Choreographer.FrameCallback 的方式计算 FPS，会出现当没有绘制界面时FPS数值偏低的情况，
        无法真实反映用户使用体验。在实际监控中，我们更关注 App 在用户产生交互后的流畅性，比如滑动时是否
        卡顿。因此，我们需要在帧率的基础上，增加一个更加细致的指标：滑动帧率。
         */
        val onScrollListener = ViewTreeObserver.OnScrollChangedListener { // 开始滑动后向 Handler 中提交一个延迟执行的任务，下次滑动后移除。
            isScrolling = true
            uiHandler.removeCallbacks(onScrollFinishRunnable)
            uiHandler.postDelayed(onScrollFinishRunnable, 100)
        }

        // FrameMetrics(android.view.FrameMetrics) 是 从Android 7.0 开始提供的新API，它提供了每帧的详细耗时数据，其中也包括当前帧的总耗时数据。
        val frameMetricsAvailableListener by lazy {
            Window.OnFrameMetricsAvailableListener { window, metrics, dropCountSinceLastInvocation ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    aggregateMetrics(window, metrics, dropCountSinceLastInvocation)
                }
            }
        }

        context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityResumed(activity: Activity) {
                if (installed.get()) {
                    activity.window.decorView.viewTreeObserver.addOnScrollChangedListener(onScrollListener)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        activity.window.addOnFrameMetricsAvailableListener(frameMetricsAvailableListener, uiHandler)
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
                activity.window.decorView.viewTreeObserver.removeOnScrollChangedListener(onScrollListener)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activity.window.removeOnFrameMetricsAvailableListener(frameMetricsAvailableListener)
                }
            }
        })
    }

    private fun doInstall() {
        Timber.d("FPSMonitor installed. with refresh rate: $refreshRate, intended frame time: $intendedFrameTimeMs")
        Choreographer.getInstance().postFrameCallback(callback)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // use FrameMetrics to get more accurate data.
            Timber.d("FrameMetrics is available.")
        }
    }

    private fun calculateFPS(frameTimeNanos: Long) {
        if (lastFrameTimeNanos == 0L) {
            lastFrameTimeNanos = frameTimeNanos
            return
        }

        // 上一帧的耗时
        val frameIntervalMs = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000F
        // 计算出掉帧数
        val droppedCount = (frameIntervalMs / intendedFrameTimeMs).toInt()
        // 累计掉帧数和帧数
        totalFrameCount++
        totalDropFrameCount += droppedCount
        // 每帧耗时的异常数据处理，每帧耗时应不低于理论帧间隔时间。
        totalFrameDurationMs += max(intendedFrameTimeMs, frameIntervalMs)
        val now = System.currentTimeMillis()
        if (now - lastFPSReportTimeMs >= REPORT_INTERVAL) {
            // FPS 需要数据校准，每秒的帧数不超过屏幕刷新率，不能固定为60。
            val fps = min(refreshRate, totalFrameCount / totalFrameDurationMs * 1_000)
            lastFPSReportTimeMs = now
            reportFPS(fps)
            totalFrameCount = 0
            totalDropFrameCount = 0
            totalFrameDurationMs = 0F
        }

        lastFrameTimeNanos = frameTimeNanos
    }

    private fun reportFPS(fps: Float) {
        Timber.d("FPS: $fps, drop frame count: $totalDropFrameCount, isScrolling: $isScrolling")
    }

    /**
     * 将连续丢帧超过 700ms 认为是冻帧，此时可以收集堆栈等信息。
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun aggregateMetrics(window: Window, metrics: FrameMetrics, dropCountSinceLastInvocation: Int) {
        Timber.w("FrameMetricsAvailableListener: metrics: ${metrics.getMetric(FrameMetrics.TOTAL_DURATION) / 1_000_000F}ms")
    }

    companion object {
        private const val REPORT_INTERVAL = 1000L
    }

}