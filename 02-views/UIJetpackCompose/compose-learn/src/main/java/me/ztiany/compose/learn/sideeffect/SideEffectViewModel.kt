package me.ztiany.compose.learn.sideeffect

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideEffectViewModel @Inject constructor() : ViewModel() {

    val uiState = mutableStateOf(UiState<List<String>>(false, null))

    init {
        viewModelScope.launch {
            delay(3000)
            uiState.value = UiState(true, null)
        }
    }

}

data class UiState<T>(
    val hasError: Boolean,
    val data: T?
)