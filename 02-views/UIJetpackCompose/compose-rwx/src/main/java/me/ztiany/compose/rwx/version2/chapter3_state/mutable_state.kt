package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
private fun MutableState() {
    val text = remember {
        mutableStateOf("1")
    }

    Text(text = text.value)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            text.value = (text.value.toInt() + 1).toString()
        }
    }
}