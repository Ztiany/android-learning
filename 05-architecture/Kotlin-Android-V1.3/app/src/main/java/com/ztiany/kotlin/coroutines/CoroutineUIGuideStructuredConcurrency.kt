package com.ztiany.kotlin.coroutines

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * [使用协程进行 UI 编程指南](https://github.com/hltj/kotlinx.coroutines-cn/blob/master/ui/coroutines-guide-ui.md)
 *
 * 结构化并发，生命周期以及协程父子层级结构
 */
@ExperimentalCoroutinesApi
abstract class ScopedAppActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        super.onDestroy()
        cancel() // CoroutineScope.cancel
    }

}

@SuppressLint("Registered")
@ExperimentalCoroutinesApi
class TestActivity : ScopedAppActivity() {

    fun asyncShowData() = launch {
        // Activity 的 job 作为父结构时，这里将在 UI 上下文中被调用
        // 实际实现
    }

    suspend fun showIOData() {
        val deferred = async(Dispatchers.IO) {
            // 实现
        }
        withContext(Dispatchers.Main) {
            val data = deferred.await()
            // 在 UI 中展示数据
        }
    }

}

@SuppressLint("Registered")
@ExperimentalCoroutinesApi
class ActivityWithPresenters : ScopedAppActivity() {

    fun init() {
        val presenter = Presenter()
        val presenter2 = ScopedPresenter(this)
    }

}

private class Presenter {

    suspend fun loadData() = coroutineScope {
        // 外部 activity 的嵌套作用域
    }

    fun loadData(uiScope: CoroutineScope) = uiScope.launch {
        // 在 UI 作用域中调用
    }

}

private class ScopedPresenter(scope: CoroutineScope) : CoroutineScope by scope {

    fun loadData() = launch {
        // 作为 ActivityWithPresenters 的作用域的扩展
    }

}

private suspend fun CoroutineScope.launchInIO() = launch(Dispatchers.IO) {
    // 在调用者的作用域中启动，但使用 IO 调度器
}