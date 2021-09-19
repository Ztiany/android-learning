package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficialFlowHomeViewModel @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        private val repository: FlowRepository
) : ViewModel() {

    private val _latestNews = MutableLiveData<List<String>>()
    val latestNews: LiveData<List<String>>
        get() = _latestNews

    ///////////////////////////////////////////////////////////////////////////
    // Example1：Collect flow in a ViewModel and distribute data by a LiveData.
    ///////////////////////////////////////////////////////////////////////////
    init {
        viewModelScope.launch {
            repository.latestNews
                    .catch { error ->
                        //notify when an error happens.
                        Log.e(FLOW_TAG, "OfficialFlowHomeViewModel.latestNews: ", error)
                    }
                    .collect {
                        //use LiveData to distribute data.
                        _latestNews.value = it
                    }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // callbackFlow
    ///////////////////////////////////////////////////////////////////////////
    fun loadUser() = liveData {
        repository.getUserEvents()
                .catch { error ->
                    Log.e(FLOW_TAG, "OfficialFlowHomeViewModel.getUserEvents: ", error)
                }
                .collect {
                    emit(it)
                }
    }

    ///////////////////////////////////////////////////////////////////////////
    // StateFlow
    ///////////////////////////////////////////////////////////////////////////
    // Backing property to avoid state updates from other classes
    // StateFlow 是热数据流：从此类数据流收集数据不会触发任何提供方代码，当新使用方开始从数据流中收集数据时，它将接收信息流中的最近一个状态及任何后续状态。
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.favoriteLatestNews
                    // Update View with the latest favorite news
                    // Writes to the value property of MutableStateFlow,
                    // adding a new element to the flow and updating all
                    // of its collectors
                    .collect { favoriteNews ->
                        _uiState.value = LatestNewsUiState.Success(favoriteNews)
                    }
        }
    }

    // Represents different states for the LatestNews screen
    sealed class LatestNewsUiState {
        data class Success(val news: List<String>) : LatestNewsUiState()
        data class Error(val exception: Throwable) : LatestNewsUiState()
    }

}