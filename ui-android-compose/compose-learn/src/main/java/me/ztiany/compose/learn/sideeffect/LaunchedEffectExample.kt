package me.ztiany.compose.learn.sideeffect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchedEffectExample() {
    val uiState: MutableState<UiState<List<String>>> = hiltViewModel<SideEffectViewModel>().uiState
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "LaunchedEffectExample") })
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            // If the UI state contains an error, show snackbar
            Timber.d("LaunchedEffectExample hasError?")
            if (uiState.value.hasError) {
                Timber.d("LaunchedEffectExample: hasError = true")
                // `LaunchedEffect` will cancel and re-launch if
                // `scaffoldState.snackbarHostState` changes
                LaunchedEffect(snackbarHostState) {
                    // Show snackbar using a coroutine, when the coroutine is cancelled the
                    // snackbar will automatically dismiss. This coroutine will cancel whenever
                    // `state.hasError` is false, and only start when `state.hasError` is true
                    // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
                    snackbarHostState.showSnackbar(
                        message = "Error message",
                        actionLabel = "Retry message"
                    )
                }
            }
            Text(text = "3 seconds late, a snackbar will be showing.")
        }
    }
}