package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
private fun StateHoisting() {
    // 状态
    val field = remember {
        mutableStateOf("")
    }

    // 无状态的 TextField
    TextField(value = field.value, onValueChange = {
        field.value = it
    })
}