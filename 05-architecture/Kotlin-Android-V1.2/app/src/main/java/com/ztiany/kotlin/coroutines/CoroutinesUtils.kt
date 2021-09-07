package com.ztiany.kotlin.coroutines

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

/**
 * - DeferredCoroutine 任务创建后会立即启动
 * - LazyDeferredCoroutine 任务创建后new的状态，要等用户调用 start() or join() or await()去启动他
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-21 00:37
 */
fun <T> launchUI(block: suspend CoroutineScope.() -> T, uiBlock: suspend (T) -> Unit): Deferred<T> {

    val deferred = async(CommonPool, block = block)

    launch(UI) {
        uiBlock(deferred.await())
    }

    return deferred
}


fun launchUI(start: CoroutineStart = CoroutineStart.DEFAULT, parent: Job? = null, block: suspend CoroutineScope.() -> Unit) =
        launch(UI, true, block)
