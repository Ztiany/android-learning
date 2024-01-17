package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun S302_AutoUpdate() {
    val test = remember {
        mutableStateOf("1")
    }

    Box(Modifier.clickable {
        // 这个写事件没有发生在组合过程，这不会通知到 snapshot.writeObserver，这里的自动更新由【应用】事件实现。
        test.value = "2"
    }) {
        // 这个对 test 的读被记录
        Text(text = test.value)
        // 这个对 test 的写发生在组合过程中，这个会使得 snapshot.writeObserver 发送通知。这里就会将这个区域标记为需要更新。
        test.value = "3"
    }
}