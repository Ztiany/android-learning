package me.ztiany.compose.foundation.widgets

import android.content.Context
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

//参考：https://jetpackcompose.cn/docs/elements/slider
@Composable
fun SliderExample(context: Context) {
    var progress by remember { mutableStateOf(0f) }

    Slider(
        value = progress,
        colors = SliderDefaults.colors(
            thumbColor = Color.White, // 圆圈的颜色
            activeTrackColor = Color(0xFF0079D3)
        ),
        onValueChange = {
            progress = it
        },
    )
}