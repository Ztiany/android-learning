package com.ztiany.kotlin.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CoroutineWithGlobalScopeViewModel : ViewModel() {

    private val repository = DummyRepository()

    fun startLoad() {
        GlobalScope.launch {
            Log.d(this::class.java.simpleName, repository.doSomethingVeryLong())
        }
    }

}

private class DummyRepository {

    suspend fun doSomethingVeryLong(): String {
        return withContext(Dispatchers.Default) {
            delay(60 * 1000)
            "Result"
        }
    }

}