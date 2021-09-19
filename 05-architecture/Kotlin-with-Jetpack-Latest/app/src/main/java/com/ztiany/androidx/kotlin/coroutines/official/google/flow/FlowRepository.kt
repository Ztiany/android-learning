package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.random.Random

class FlowRepository @Inject constructor() {

    ///////////////////////////////////////////////////////////////////////////
    // Returning Flow
    ///////////////////////////////////////////////////////////////////////////

    private val refreshIntervalMs: Long = 5000

    val latestNews: Flow<List<String>> = flow {
        while (true) {
            val latestNews = fetchLatestNews()
            emit(latestNews) // Emits the result of the request to the flow
            delay(refreshIntervalMs) // Suspends the coroutine for some time
        }
    }.map { list ->
        list.filter { item ->
            item.toInt() % 2 == 0
        }
    }.onEach {
        saveToLocal(it)
    }.catch { error ->
        Log.e(FLOW_TAG, "FlowRepository: ", error)
        // If an error happens, emit the last cached values.【but catch can not be used by times, which means catch in ViewModel is useless.】
        emit(loadCache())
    }.flowOn(Dispatchers.IO)

    private fun saveToLocal(newData: List<String>) {
        //dummy
    }

    private fun fetchLatestNews(): List<String> {
        if (Random.nextBoolean()) {
            throw NullPointerException()
        }
        return List(Random.nextInt(30)) {
            it.toString()
        }
    }

    private fun loadCache(): List<String> {
        Log.e(FLOW_TAG, "loadCache")
        return List(Random.nextInt(10)) {
            it.toString()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // callbackFlow
    ///////////////////////////////////////////////////////////////////////////
    //与 flow 构建器不同，callbackFlow 允许通过 send 函数从不同 CoroutineContext 发出值，或者通过 offer 函数在协程外发出值。
    fun getUserEvents(): Flow<String> = callbackFlow {
        val controller = loadUser(object : UserLoadedCallback {
            override fun onUserLoaded(name: String) {
                trySend(name)
            }

            override fun onErrorOccurred(throwable: Throwable) {
                //notify error.
                close(throwable)
            }

        })

        awaitClose { controller.cancel() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // StateFlow
    ///////////////////////////////////////////////////////////////////////////
    val favoriteLatestNews: Flow<List<String>> = flow {

    }
}

