package com.ztiany.kotlin.coroutines

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-29 00:23
 */
class ArchViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + exceptionHandler

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}