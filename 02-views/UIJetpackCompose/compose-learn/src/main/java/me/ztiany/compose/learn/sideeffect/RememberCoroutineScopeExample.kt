package me.ztiany.compose.learn.sideeffect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberCoroutineScopeExample() {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "LaunchedEffectExample") })
        },
    ) {

        Box(modifier = Modifier.padding(it)) {
            // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
            val scope = rememberCoroutineScope()
            // Content
            Column {
                Button(
                    onClick = {
                        // Create a new coroutine in the event handler to show a snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar("Something happened!")
                        }
                    }
                ) {
                    Text("Press me")
                }
            }
        }

    }
}