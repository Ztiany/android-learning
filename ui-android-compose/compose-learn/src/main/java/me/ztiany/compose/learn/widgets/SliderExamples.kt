package me.ztiany.compose.learn.widgets

import android.content.Context
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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