package me.ztiany.compose.learn.sideeffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun RememberUpdatedStateExample() {
    InternalExample {
        Timber.d("onTimeout")
    }
}

@Composable
private fun InternalExample(onTimeout: () -> Unit) {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {//这里传一个 ture，则 LaunchedEffect 不会因为重组而重启，但是同样需要注意这样做的危害性。
        delay(3000)
        currentOnTimeout()
    }
}
