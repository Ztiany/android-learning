package me.ztiany.compose.rwx

import androidx.compose.runtime.Composable
import me.ztiany.compose.rwx.state.MutableStateSample

internal fun buildEntranceList() = listOf(
    "MutableState"
)

@Composable
internal fun InvokeComposable(name: String) {
    when (name) {
        "MutableState" -> MutableStateSample(name)
    }
}

