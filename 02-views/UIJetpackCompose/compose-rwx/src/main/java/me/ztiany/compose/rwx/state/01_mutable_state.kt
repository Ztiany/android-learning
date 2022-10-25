package me.ztiany.compose.rwx.state

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun MutableStateSample(name: String) {
    val content = remember(name) {
        "Result: $name"
    }

    Text(text = content)
}