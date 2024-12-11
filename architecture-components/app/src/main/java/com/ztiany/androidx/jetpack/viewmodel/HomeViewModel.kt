package com.ztiany.androidx.jetpack.viewmodel

import androidx.lifecycle.*
import com.ztiany.androidx.common.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _liveData = MutableLiveData<DemoState>()
    val liveData: LiveData<DemoState>
        get() = _liveData

    private val _singleLiveData = SingleLiveData<DemoState>()
    val singleLiveData: LiveData<DemoState>
        get() = _singleLiveData

    private val _sharedFlow0 = MutableSharedFlow<DemoState>(0)
    val sharedFlow0: Flow<DemoState>
        get() = _sharedFlow0

    private val _sharedFlow1 = MutableSharedFlow<DemoState>(1)
    val sharedFlow1: Flow<DemoState>
        get() = _sharedFlow1

    private val _stateFlow = MutableStateFlow(DemoState(false, ""))
    val stateFlow: Flow<DemoState>
        get() = _stateFlow

    private val _channel = Channel<DemoState>(Channel.BUFFERED)
    val channel: Flow<DemoState>
        get() = _channel.receiveAsFlow()

    private val _sharedFlowRepeat0 = MutableSharedFlow<DemoState>(0)
    val sharedFlowRepeat0: Flow<DemoState>
        get() = _sharedFlowRepeat0

    private val _sharedFlowRepeat1 = MutableSharedFlow<DemoState>(1)
    val sharedFlowRepeat1: Flow<DemoState>
        get() = _sharedFlowRepeat1

    private val _stateFlowRepeat = MutableStateFlow(DemoState(false, ""))
    val stateFlowRepeat: Flow<DemoState>
        get() = _stateFlowRepeat

    private val _channelRepeat = Channel<DemoState>(Channel.BUFFERED)
    val channelRepeat: Flow<DemoState>
        get() = _channelRepeat.receiveAsFlow()

    private val _sharedFlowRepeat0View = MutableSharedFlow<DemoState>(0)
    val sharedFlowRepeat0View: Flow<DemoState>
        get() = _sharedFlowRepeat0View

    private val _sharedFlowRepeat1View = MutableSharedFlow<DemoState>(1)
    val sharedFlowRepeat1View: Flow<DemoState>
        get() = _sharedFlowRepeat1View

    private val _stateFlowRepeatView = MutableStateFlow(DemoState(false, ""))
    val stateFlowRepeatView: Flow<DemoState>
        get() = _stateFlowRepeatView

    private val _channelRepeatView = Channel<DemoState>(Channel.BUFFERED)
    val channelRepeatView: Flow<DemoState>
        get() = _channelRepeatView.receiveAsFlow()

    fun doSomething() {
        viewModelScope.launch {
            notifyLoading()
            delay(5000)
            notifySuccess()
        }
    }

    fun doSomethingLiveData(): LiveData<DemoState> {
        val data = MutableLiveData<DemoState>()
        data.value = DemoState(true, "")
        viewModelScope.launch {
            delay(5000)
            data.postValue(DemoState(false, "Succeeded"))
        }
        return data
    }

    fun doSomethingSingleLiveData(): LiveData<DemoState> {
        val data = SingleLiveData<DemoState>()
        data.value = DemoState(true, "")
        viewModelScope.launch {
            delay(5000)
            data.postValue(DemoState(false, "Succeeded"))
        }
        return data
    }

    private suspend fun notifyLoading() {
        val demoState = DemoState(true, "")
        _liveData.postValue(demoState)
        _singleLiveData.postValue(demoState)

        _sharedFlow0.emit(demoState)
        _sharedFlow1.emit(demoState)
        _stateFlow.emit(demoState)
        _channel.send(demoState)

        _sharedFlowRepeat0.emit(demoState)
        _sharedFlowRepeat1.emit(demoState)
        _stateFlowRepeat.emit(demoState)
        _channelRepeat.send(demoState)

        _sharedFlowRepeat0View.emit(demoState)
        _sharedFlowRepeat1View.emit(demoState)
        _stateFlowRepeatView.emit(demoState)
        _channelRepeatView.send(demoState)
    }

    private suspend fun notifySuccess() {
        val demoState = DemoState(false, "Succeeded")

        _liveData.postValue(demoState)
        _singleLiveData.postValue(demoState)

        _sharedFlow0.emit(demoState)
        _sharedFlow1.emit(demoState)
        _stateFlow.emit(demoState)
        _channel.send(demoState)

        _sharedFlowRepeat0.emit(demoState)
        _sharedFlowRepeat1.emit(demoState)
        _stateFlowRepeat.emit(demoState)
        _channelRepeat.send(demoState)

        _sharedFlowRepeat0View.emit(demoState)
        _sharedFlowRepeat1View.emit(demoState)
        _stateFlowRepeatView.emit(demoState)
        _channelRepeatView.send(demoState)
    }

}

data class DemoState(
    val isLoading: Boolean = false,
    val data: String = ""
)