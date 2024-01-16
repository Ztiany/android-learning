package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
    ParentDataModifier 的原理：
        1. 只需要关注 ParentDataModifier 在 NodeChain 中的位置即可。
        2. 然后就是 NodeCoordinator 中的 parentData 实现。
 */
@Composable
fun S512_ParentDataModifier() {
    Row {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Blue)
                .weight(1F)
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Green)
        )
    }
}