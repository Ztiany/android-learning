package me.ztiany.compose.learn.animation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val TAG = "AnimatableExample"

/** 演示 Animatable 的使用*/
@SuppressLint("UnrememberedAnimatable")
@Composable
fun AnimatableExample() {
    /*
    Animatable 是一个数值包装器，它的 animateTo 方法可以根据数值的变化设置动画效果，animate*AsState 背后就是基于 Animatable 实现的。

    Animatable 中包括 animateTo 在内的许多API都是挂起函数，需要在 CoroutineScope 中执行，可以使用 LaunchedEffect 为其提供所需的环境。

    Animatable 是 animate*AsState 的基础，因此相对于 animate*AsState，它具有更多灵活的能力，这体现在以下几个方面：

            首先，Animatable 允许设置一个不同的初始值，比如可以将 IconButton 的初始颜色设置为一个不同于 Gray 和 Red 的任意颜色，然后通过 animateTo 将其改变为目标颜色。
            其次，Animatable 除了 animateTo 之外，还提供了不少其他方法，比如：
                    snapTo 可以立即到达目标值，中间没有过渡值，可以在需要跳过动画的场景中使用这个方法。
                    animateDecay 可以启动的一个衰减动画，这在 fling 等场景中非常有用。
     */
    var change by remember { mutableStateOf(false) }
    var flag by remember { mutableStateOf(false) }

    // Animatable 传入名为 Dp.VectorConverter 的参数，这是一个针对 Dp 类型的 TwoWayConverter。Animatable 可以直接传入 Float 或 Color 类型的值，
    // 当传入其他类型时，需要同时指定对应的 TwoWayConverter。Compose 为常用数据类型都提供了对应的 TwoWayConverter 实现，直接传入即可，
    // 如下面代码中的 Dp. VectorConverter。
    val buttonSize = remember { Animatable(24.dp, Dp.VectorConverter) }
    val buttonColor = remember { Animatable(Color.Gray) }

    // LaunchedEffect 会在 onActive 时被执行，此时要确保 animateTo 的 targetValue 与 Animatable 的默认值相同，比如例子中 buttonSizeVariable 的初始值是 24dp,
    // change 对应的 targetValue 也是 24dp，否则在页面首次渲染时，便会出现 24dp-> 32dp 的过渡动画，这会与我们预期的动画效果不符。
    LaunchedEffect(flag) {
        Log.d(TAG, "AnimatableExample-LaunchedEffect-flag is running.")
        buttonColor.animateTo(if (flag) Color.Red else Color.Gray)
    }

    LaunchedEffect(change) {
        Log.d(TAG, "AnimatableExample-LaunchedEffect-change is running.")
        buttonSize.animateTo(if (change) 32.dp else 24.dp)
    }

    if (buttonSize.value == 32.dp) {
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
            modifier = Modifier.size(buttonSize.value),
            tint = buttonColor.value
        )
    }

}