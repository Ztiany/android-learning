package com.ztiany.kotlin.coroutines.ex

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * modify form [Lifecycle-Coroutines-Extension](https://github.com/fengzhizi715/Lifecycle-Coroutines-Extension)
 */
open class LifecycleCoroutineListener(
        private val job: Job,
        private val cancelEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        handleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop(owner: LifecycleOwner) {
        handleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        handleEvent(Lifecycle.Event.ON_DESTROY)
    }

    private fun handleEvent(e: Lifecycle.Event) {
        if (e == cancelEvent && !job.isCancelled) {
            job.cancel()
        }
    }

}

fun <T> GlobalScope.asyncWithLifecycle(
        lifecycleOwner: LifecycleOwner,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
): Deferred<T> {

    val deferred = GlobalScope.async(context, start) {
        block()
    }

    lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))

    return deferred
}

/**创建 GlobalScope 的扩展函数 bindWithLifecycle，它的协程 block 在调用时绑定生命周期。*/
fun <T> GlobalScope.bindWithLifecycle(
        lifecycleOwner: LifecycleOwner,
        block: CoroutineScope.() -> Deferred<T>
): Deferred<T> {

    val deferred = block.invoke(this)

    lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))

    return deferred
}

infix fun <T> Deferred<T>.then(block: (T) -> Unit): Job {
    return GlobalScope.launch(context = Dispatchers.Main) {
        block(this@then.await())
    }
}

infix fun <T, R> Deferred<T>.thenAsync(block: (T) -> R): Deferred<R> {
    return GlobalScope.async(context = Dispatchers.Main) {
        block(this@thenAsync.await())
    }
}

suspend fun <T> Deferred<T>.awaitOrNull(timeout: Long = 0L): T? {

    return try {

        if (timeout > 0) {
            withTimeout(timeout) {
                this@awaitOrNull.await()
            }
        } else {
            this.await()
        }

    } catch (e: Exception) {
        Log.e("Deferred", e.message)
        null
    }

}