package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Crossfade 用于两个组件之间的切换，它会根据两个组件的可见性来控制它们的显示与隐藏。也是基于 Transition 实现的。
 * Crossfade 只能做淡入淡出的动画。
 */
@Composable
fun S415_Crossfade() {
    var shown by remember {
        mutableStateOf(true)
    }
    Column {
        Crossfade(targetState = shown, label = "Crossfade") {
            if (it) {
                Box1()
            } else {
                Box2()
            }
        }
        Button(onClick = {
            shown = !shown
        }) {
            Text(text = "切换")
        }
    }
}

@Composable
private fun Box1() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Red)
    )
}

@Composable
private fun Box2() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Green)
    )
}
