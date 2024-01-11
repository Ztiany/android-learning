package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
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
    InfiniteRepeatableSpec 表示无限重复动画，它的参数 animation 是一个动画配置，这个动画配置会被无限重复执行。

    InfiniteRepeatableSpec 本质上和 RepeatableSpec 是一样的。
 */
@Composable
fun S408_AnimationSpec_InfiniteRepeatableSpec() {

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
        // infiniteRepeatable 只有在其所在的协程结束后才会结束，所以这里的动画会一直执行下去。
        sizeAnimatable.animateTo(
            size,
            infiniteRepeatable(
                animation = TweenSpec(
                    durationMillis = 450,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Yellow)
            .clickable {
                big = !big
            })
}