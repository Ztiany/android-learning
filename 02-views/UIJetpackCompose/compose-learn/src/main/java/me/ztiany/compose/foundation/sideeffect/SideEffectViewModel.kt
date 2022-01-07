package me.ztiany.compose.foundation.sideeffect

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SideEffectViewModel : ViewModel() {

    val uiState = mutableStateOf(UiState<List<String>>(false, null))

    init {
        viewModelScope.launch {
            delay(3000)
            uiState.value = UiState<List<String>>(true, null)
        }
    }

}

data class UiState<T>(
    val hasError: Boolean,
    val data: T?
)