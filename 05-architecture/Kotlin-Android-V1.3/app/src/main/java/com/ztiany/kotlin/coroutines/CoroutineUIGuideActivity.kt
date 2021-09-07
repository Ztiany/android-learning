package com.ztiany.kotlin.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.ztiany.kotlin.R
import kotlinx.android.synthetic.main.activity_coroutine_guide.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import org.jetbrains.anko.AnkoLogger

/**  [使用协程进行 UI 编程指南](https://github.com/hltj/kotlinx.coroutines-cn/blob/master/ui/coroutines-guide-ui.md)*/
@SuppressLint("SetTextI18n")
@ObsoleteCoroutinesApi
class CoroutineUIGuideActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        @JvmStatic
        private val TAG = CoroutineUIGuideActivity::class.simpleName
    }

    private var mJob = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_guide)
        setSupportActionBar(toolbarCoroutineGuide)
        setupActorViews()
        setupAdvanceViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    fun cancel(view: View) {
        mJob.cancel()
    }

    private fun bindLifecycle(): Job {
        if (mJob.isCancelled || mJob.isCompleted) {
            mJob = Job()
        }
        return mJob
    }

    //1---------------------------------------------------------

    fun startCounter(view: View) {
        GlobalScope.launch(Dispatchers.Main + bindLifecycle()) {
            // 在主线程中启动协程
            for (i in 10 downTo 1) { // 从 10 到 1 的倒计时
                tvCoroutineGuideValue.text = "Countdown $i ..." // 更新文本
                delay(500) // 等待半秒钟
            }
            tvCoroutineGuideValue.text = "Done!"
        }
    }

    //2---------------------------------------------------------

    private fun setupActorViews() {
        btnCoroutineGuideActor1.onClick {
            for (i in 10 downTo 1) { // 从 10 到 1 的倒计时
                tvCoroutineGuideValue.text = "Countdown $i ..." // 更新文本
                delay(500) // 等待半秒钟
            }
            tvCoroutineGuideValue.text = "Done!"
        }

        btnCoroutineGuideActor2.onClickConflated {
            for (i in 10 downTo 1) { // 从 10 到 1 的倒计时
                tvCoroutineGuideValue.text = "Countdown $i ..." // 更新文本
                delay(500) // 等待半秒钟
            }
            tvCoroutineGuideValue.text = "Done!"
        }
    }

    /*使用一个 actor 来执行任务而不应该进行并发。*/
    private fun View.onClick(action: suspend (View) -> Unit) {
        // 启动一个 actor
        val eventActor = GlobalScope.actor<View>(Dispatchers.Main + bindLifecycle()) {
            for (event in channel) {
                action(event)
            }
        }

        // 设置一个监听器来启用 actor
        setOnClickListener {
            // SendChannel 上的 offer 函数不会等待。它会立即将一个元素发送到 actor， 如果可能的话，或者丢弃一个元素。
            //反复点击圆形按钮。当倒计时动画进行中时， 点击动作会被忽略。这会发生的原因是 actor 正忙于执行而不会从通道中接收元素。
            // 默认的，一个 actor 的邮箱由 RendezvousChannel 支持，只有当 receive 在运行中的时候 offer 操作才会成功。
            val offer = eventActor.offer(it)
            Log.d(TAG, "offer result = $offer")//print true once, print false multi
        }
    }

    //有时处理最近的事件更合适，而不是在我们忙于处理前一个事件的时候忽略事件。
    // actor 协程构建器接收一个可选的 capacity 参数来控制此 actor 用于其邮箱的通道的实现。
    private fun View.onClickConflated(action: suspend (View) -> Unit) {
        // 启动一个 actor
        val eventActor = GlobalScope.actor<View>(
                Dispatchers.Main + bindLifecycle(),
                capacity = Channel.CONFLATED) {
            for (event in channel) {
                action(event)
            }
        }

        // 设置一个监听器来启用 actor
        setOnClickListener {
            //现在，当动画运行中时如果这个圆形按钮被点击，动画将在结束后重新运行。仅仅一次。
            // 在倒数进行中时，重复点击将被合并 ，只有最近的事件才会被处理。
            val offer = eventActor.offer(it)
            Log.d(TAG, "offer result = $offer")//print true
        }
    }

    //3---------------------------------------------------------

    private fun fibBlocking(x: Int): Int = if (x <= 1) x else fibBlocking(x - 1) + fibBlocking(x - 2)
    private suspend fun fib(x: Int): Int = withContext(Dispatchers.Default) {
        fibBlocking(x)
    }

    /*UI冻结问题*/
    fun startUiBlockProblem(view: View) {
        var result = "none" // 最后一个结果
        // counting animation
        GlobalScope.launch(Dispatchers.Main) {
            var counter = 0
            while (true) {
                tvCoroutineGuideValue.text = "${++counter}: $result"
                delay(100) // 每 100 毫秒更新一次文本
            }
        }

        // 在每次点击时计算下一个斐波那契数
        var x = 1
        //尝试在这个例子中点击圆形按钮。在大约 30 到 40 次点击 btnCoroutineGuideActor4 后我们的简单计算将会变得非常缓慢并且你会立即看到
        // UI 主线程是如何冻结的，因为动画会在 UI 冻结期间停止运行。
        btnCoroutineGuideActor4.onClickConflated {
            result = "fibBlocking($x) = ${fibBlocking(x)}"
            x++
        }

        //现在你可以享受全速的，不阻塞 UI 主线程的简单斐波那契计算。 我们需要的都在 withContext(Dispatchers.Default) 中。
        //注意，由于在我们的代码中 fib 函数是被单 actor 调用的，这里在任何给定时间最多只会进行一个计算，所以这段代码具有天然的资源利用率限制。
        // 它会饱和占用最多一个 CPU 核心。
        btnCoroutineGuideActor5.onClickConflated {
            result = "fib($x) = ${fib(x)}"
            x++
        }
    }

    //4---------------------------------------------------------高级主题：没有调度器时在 UI 事件处理器中启动协程
    @ExperimentalCoroutinesApi
    private fun setupAdvanceViews() {
        //当我们运行这段代码，下面的信息将会在控制台中打印：
        /*
        Before launch
        After launch
        Inside coroutine
        After delay
         */
        btnCoroutineGuideActor6.setOnClickListener {
            println("Before launch")
            GlobalScope.launch(Dispatchers.Main) {
                println("Inside coroutine")
                delay(100)
                println("After delay")
            }
            println("After launch")
        }

        //在当协程从事件处理程序启动时周围没有其它代码这种特殊案例中， 这个额外的调度确实增加了额外的开销，而没有带来任何额外的价值。
        // 在这个案例中一个可选的 CoroutineStart 参数可赋值给 launch、async 以及 actor 协程构建器 来进行性能优化。
        // 将它的值设置为 CoroutineStart.UNDISPATCHED 可以更有效率的开始立即执行协程并直到第一个挂起点，如同下面的例子所示：
        /*
        Before launch
        Inside coroutine
        After launch
        After delay
         */
        btnCoroutineGuideActor7.setOnClickListener {
            println("Before launch")
            GlobalScope.launch(Dispatchers.Main, CoroutineStart.UNDISPATCHED) {
                // <--- 通知这次改变
                println("Inside coroutine")
                delay(100) // <--- 这里是协程挂起的地方
                println("After delay")
            }
            println("After launch")
        }
    }

}
