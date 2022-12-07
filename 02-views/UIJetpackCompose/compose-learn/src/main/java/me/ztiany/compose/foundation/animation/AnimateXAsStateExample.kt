package me.ztiany.compose.foundation.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/** 演示 AnimateXAsState 的使用*/
@Composable
fun AnimateXAsStateExample() {
    /*
    animated*AsState 是最常用的高级别API之一，它类似于传统视图中的属性动画，可以自动完成从当前值到目标值过渡的估值计算。

    以 animateColorAsState 为例，它将 Color 转成一个可以在 Composable 中访问的 State。Color 的变化会触发 Composable 重组，从而完成动画效果。

    下面例子：
            • change 更新为 ture：buttonSize 由 24dp 过渡到 32dp。
            • flag 取反：buttonColor 由 Gray过 渡到 Red。
        当 buttonSize 到达 targetValue 32dp 时，change 被重置为false, buttonSize 再次恢复到24dp。
        再次点击则按照同样的动画路径，size由小到大再到小，color由红色过渡到灰色。

        Compose 为常用的数据类型都提供了animate*AsState 方法，例如 Float、Color、Dp、Size、Bounds、Offset、Rect、Int、IntOffset 和 InSize 等，
        对于无法直接估值计算的数据类型，可以使用通用类型的 animateValueAsState，并自行实现 TwoWayConverter 估值计算器。
     */
    var change by remember { mutableStateOf(false) }
    var flag by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue =  if (flag) Color.Red else Color.Gray
    )

    val buttonSize by animateDpAsState(
        targetValue = if (change) 32.dp else 24.dp
    )

    if (buttonSize == 32.dp) {
        change = false
    }

    IconButton(
        onClick = {
            change = true
            flag = !flag
        }
    ) {
        Icon(
            Icons.Rounded.Favorite,
            contentDescription = null,
            modifier = Modifier.size(buttonSize),
            tint = buttonColor
        )
    }
}