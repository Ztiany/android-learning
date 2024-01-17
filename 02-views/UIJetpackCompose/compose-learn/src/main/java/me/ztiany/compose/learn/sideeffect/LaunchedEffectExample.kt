package me.ztiany.compose.learn.sideeffect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LaunchedEffectExample() {
    val uiState: MutableState<UiState<List<String>>> = hiltViewModel<SideEffectViewModel>().uiState
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text(text = "LaunchedEffectExample") })
        },
    ) {

        Box(modifier = Modifier.padding(it)) {
            // If the UI state contains an error, show snackbar
            if (uiState.value.hasError) {
                // `LaunchedEffect` will cancel and re-launch if
                // `scaffoldState.snackbarHostState` changes
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    // Show snackbar using a coroutine, when the coroutine is cancelled the
                    // snackbar will automatically dismiss. This coroutine will cancel whenever
                    // `state.hasError` is false, and only start when `state.hasError` is true
                    // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Error message",
                        actionLabel = "Retry message"
                    )
                }
            }
            Text(text = "3 seconds late, a snackbar will be showing.")
        }
    }


}