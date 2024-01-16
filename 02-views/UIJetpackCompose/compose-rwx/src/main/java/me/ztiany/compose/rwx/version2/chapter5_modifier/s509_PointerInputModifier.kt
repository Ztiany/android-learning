package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@Composable
fun S509_PointerInputModifier() {
    Box(
        Modifier
            .size(100.dp)
            // 与 DrawModifier 一样，pointerInput 作用范围由它下游最近的布局操作符决定。
            .pointerInput(Unit) {

            }
            .size(120.dp)
    ) {

    }
}
