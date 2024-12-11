package me.ztiany.compose.facility.state


import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.state.StateD
import com.android.base.foundation.state.emitData
import com.android.base.foundation.state.emitError
import com.android.base.foundation.state.emitLoading
import com.android.base.foundation.state.setData
import com.android.base.foundation.state.setError
import com.android.base.foundation.state.setLoading
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

suspend fun <T> FlowCollector<StateD<T>>.syncCallState(call: suspend () -> CallResult<T>) {
    emit(StateD.loading())
    call.invoke().intoStateFlowCollector(this)
}

suspend fun <T> MutableSharedFlow<StateD<T>>.syncCallState(call: suspend () -> CallResult<T>) {
    emitLoading()
    call.invoke().intoSharedStateFlow(this)
}

suspend fun <T> MutableStateFlow<StateD<T>>.syncCallState(call: suspend () -> CallResult<T>) {
    setLoading()
    call.invoke().intoStateFlow(this)
}

suspend fun <T> MutableLiveData<StateD<T>>.syncCallState(call: suspend () -> CallResult<T>) {
    setLoading()
    call.invoke().intoStateLiveData(this)
}

fun <T> (suspend () -> CallResult<T>).transformToStateFlow(): Flow<StateD<T>> {
    return flow {
        emit(StateD.loading())
        invoke().intoStateFlowCollector(this)
    }
}

private suspend fun <T> CallResult<T>.intoStateFlowCollector(collector: FlowCollector<StateD<T>>) {
    onSuccess {
        collector.emit(StateD.success(it))
    }
    onError {
        collector.emit(StateD.error(it))
    }
}

private suspend fun <T> CallResult<T>.intoSharedStateFlow(flow: MutableSharedFlow<StateD<T>>) {
    onSuccess {
        flow.emitData(it)
    }
    onError {
        flow.emitError(it)
    }
}

private fun <T> CallResult<T>.intoStateFlow(state: MutableStateFlow<StateD<T>>) {
    onSuccess {
        state.setData(it)
    }
    onError {
        state.setError(it)
    }
}

private fun <T> CallResult<T>.intoStateLiveData(state: MutableLiveData<StateD<T>>) {
    onSuccess {
        state.setData(it)
    }
    onError {
        state.setError(it)
    }
}