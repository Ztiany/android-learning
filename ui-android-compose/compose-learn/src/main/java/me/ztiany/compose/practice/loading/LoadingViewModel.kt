package me.ztiany.compose.practice.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.ztiany.compose.facility.data.SampleRepository
import me.ztiany.compose.facility.state.transformToStateFlow
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(repository: SampleRepository) : ViewModel() {

    private val uiIntent = MutableSharedFlow<LoadingIntent>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val remarkState = uiIntent.filterIsInstance<Query>()
        .flatMapMerge {
            suspend {
                repository.fakeQuery()
            }.transformToStateFlow()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.loading())

    @OptIn(ExperimentalCoroutinesApi::class)
    val updateRemarkState = uiIntent.filterIsInstance<Update>()
        .flatMapMerge {
            suspend {
                repository.fakeUpdate(it.open)
            }.transformToStateFlow()
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    fun sendIntent(intent: LoadingIntent) {
        viewModelScope.launch { uiIntent.emit(intent) }
    }

}