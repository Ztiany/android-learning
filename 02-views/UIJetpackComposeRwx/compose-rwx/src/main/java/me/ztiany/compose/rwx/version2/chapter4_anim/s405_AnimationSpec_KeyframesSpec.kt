package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/*
    KeyframesSpec 允许我们在动画的过程中选取几个关键时间点，并给出这些时间点对应的动画的完成度，动画会在这些时间点之间进行插值。
    可以将 KeyframesSpec 理解为多个 TweenSpec，然后将这些 TweenSpec 串联起来，这样就可以实现更复杂的动画效果。
 */
@Composable
fun S405_AnimationSpec_KeyframesSpec() {

    var big by remember {
        mutableStateOf(false)
    }

    val size = remember(big) {
        if (big) {
            126.dp
        } else {
            58.dp
        }
    }

    val sizeAnimatable = remember {
        Animatable(
            initialValue = size, typeConverter = Dp.VectorConverter
        )
    }

    LaunchedEffect(big) {
        /*
            使用构造函数创建 KeyframesSpec 比较麻烦，不过可以使用 KeyframesSpec.Companion.keyframes() 来简化创建过程。
         */
        sizeAnimatable.animateTo(size, animationSpec = keyframes {
            //delayMillis = 1000 // 动画延迟 1000ms
            //durationMillis = 450 // 动画总时长

            58.dp at 0 with FastOutSlowInEasing // 这是设置 0 时刻为 58（注意，这里 58 是动画的起始值），这么写是为了设置动画曲线，如果不设置动画曲线，可以直接不写。

            144.dp at 150 with FastOutLinearInEasing// 表示在 150ms 时，动画完成度为 144.dp，这里 with 指定的是 150ms 到 300ms 之间的动画曲线。
            20.dp at 300 // 表示在 300ms 时，动画完成度为 20.dp，默认的动画曲线是 LinearEasing。


            // 动画将分为 3 段：
            // 第一段从 58.dp 到 144.dp，时间是 0 到 150ms；
            // 第二段从 144.dp 到 20.dp，时间是 150ms 到 300ms；
            // 第三段从 20.dp 到 58.dp，时间是 300ms 到 450ms。
        })
    }

    // 使用了 keyframes 后，其复用性就不高了，因为控制了动画的细节，具体可以参考这段代码的演示效果，这里就不再赘述了。
    // 一个改善的方法是，在 keyframes 里面也根据 big 的值来设置动画的细节，这样就可以复用了。

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}
