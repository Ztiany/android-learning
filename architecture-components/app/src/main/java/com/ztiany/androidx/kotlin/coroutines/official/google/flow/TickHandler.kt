package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

// Class that centralizes when the content of the app needs to be refreshed
class TickHandler(
    externalScope: CoroutineScope,
    private val tickIntervalMs: Long = 5000
) {
    // Backing property to avoid flow emissions from other classes
    private val _tickFlow = MutableSharedFlow<Event<String>>(replay = 0)

    val tickFlow: SharedFlow<Event<String>> = _tickFlow

    init {
        externalScope.launch {
            while (true) {
                //刷新数据
                _tickFlow.emit(Event("Alien"))
                //间隔一段时间
                delay(tickIntervalMs)
            }
        }
    }
}

class Event<T>(val name: String)