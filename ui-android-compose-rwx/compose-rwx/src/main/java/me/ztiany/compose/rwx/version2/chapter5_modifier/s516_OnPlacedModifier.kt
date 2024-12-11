package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp

/*
    OnPlacedModifier：相当于 View 的 onLayout 完成的回调，可以拿到尺寸、布局位置以及对齐信息。
 */
@Composable
fun S516_OnPlacedModifier() {
    Column {
        Text(text = "Hello", Modifier
            .size(100.dp)
            .onPlaced {

            }
            .size(120.dp)
        )
    }
}