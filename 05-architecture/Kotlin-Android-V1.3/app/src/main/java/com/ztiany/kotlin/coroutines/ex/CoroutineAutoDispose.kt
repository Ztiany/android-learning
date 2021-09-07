package com.ztiany.kotlin.coroutines.ex

import android.os.Build
import android.view.View
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/* https://www.bennyhuo.com/2019/01/07/2019-01-07/ */

fun View.onClickAutoDisposable(
        context: CoroutineContext = Dispatchers.Main,
        handler: suspend CoroutineScope.(v: android.view.View?) -> Unit
) {
    setOnClickListener { v ->
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
            handler(v)
        }.asAutoDisposable(v)
    }
}

fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)

//我们实现了 Job 这个接口，但没有直接实现它的方法，而是用 wrapped 这个成员去代理这个接口
class AutoDisposableJob(
        private val view: View,
        private val wrapped: Job
) : Job by wrapped, View.OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        //当 View 被移除的时候，取消协程
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && view.isAttachedToWindow || view.windowToken != null

    init {
        if (isViewAttached()) {
            view.addOnAttachStateChangeListener(this)
        } else {
            cancel()
        }
        //协程执行完毕时要及时移除 listener 免得造成泄露
        invokeOnCompletion {
            view.removeOnAttachStateChangeListener(this)
        }
    }

}