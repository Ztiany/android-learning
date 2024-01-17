package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun S305_UpdateList() {
    // 对于 List，不要使用 mutableStateOf 来封装。
    var numbers1 by remember {
        mutableStateOf(listOf(1, 2, 3).toMutableList())
    }

    // 而应该使用 mutableStateListOf
    val numbers2 = remember {
        mutableStateListOf(1, 2, 3)
    }

    Button(onClick = {
        // 更新的是内部的 List，而不是 State，无法除非刷新
        numbers1.add(4)

        // 可以触发更新
        numbers1 += 4

        // 但是 Compose 提供了更好的 StateList
        numbers2.add(4)

    }) {
        Text(text = "Click Me")
    }
}