package com.ztiany.kotlin.coroutines.ex

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.Executor


/**
 * - DeferredCoroutine 任务创建后会立即启动
 * - LazyDeferredCoroutine 任务创建后new的状态，要等用户调用 start() or join() or await()去启动他
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-21 00:37
 */
fun <T> launch(dispatcher: CoroutineDispatcher = Dispatchers.Default, block: suspend CoroutineScope.() -> T, uiBlock: suspend (T) -> Unit): Deferred<T> {

    val deferred = GlobalScope.async(context = dispatcher, block = block)

    GlobalScope.launch(Dispatchers.Main) {
        uiBlock(deferred.await())
    }

    return deferred
}


fun launchUI(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit) =
        GlobalScope.launch(Dispatchers.Main, start, block)

private val rxIoExecutor = Executor { command ->
    command?.let {
        Schedulers.io().scheduleDirect(it)
    }
}
private val rxComputationExecutor = Executor { command ->
    command?.let {
        Schedulers.computation().scheduleDirect(it)
    }
}

val Io = rxIoExecutor.asCoroutineDispatcher()
val Computation = rxComputationExecutor.asCoroutineDispatcher()