package me.ztiany.compose.learn.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facility.debug.backgroundCopy
import timber.log.Timber

@Composable
fun BackgroundSizeSample() {
    var value by remember {
        mutableStateOf(200.dp)
    }
    Layout({

    }, measurePolicy = { placeables, constraints ->
        //测量链条的末端，返回给 size 修饰符
        Timber.d("measurePolicy placeables: ${placeables.size}")
        Timber.d("measurePolicy constraints: $constraints")
        Timber.d("measurePolicy layout: ${constraints.minWidth / 3}x${constraints.minHeight / 3}")
        layout(constraints.minWidth / 3, constraints.minHeight / 3) {
            Timber.d("measurePolicy layout")
        }

    }, modifier = Modifier
        .backgroundCopy("Blue", Color.Blue)
        .size(value)
        // 发现效果是在中间绘制了一个 1/3 大小的红色方块：因为画布的裁减是从中间开始的。
        .backgroundCopy("Red", Color.Red)
        .clickable {
            value += 2.dp
        })
}