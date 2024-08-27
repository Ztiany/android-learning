package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    RepeatableSpec 用于指定重复执行动画，那么重复哪个动画呢？当然是它的参数 animationSpec 所指定的动画。
 */
@Composable
fun S407_AnimationSpec_RepeatableSpec() {
    /*
        RepeatableSpec 的参考：
            iterations: Int：重复的次数，如果是 0 会报错。
            animation: DurationBasedAnimationSpec<T>：动画的配置，这里的 T 是动画值的类型。显然只能是 DurationBasedAnimationSpec 的子类型。
            repeatMode: RepeatMode = RepeatMode.Restart：重复模式，有两个值：
                Restart：重复时，每次都从起始值开始。
                Reverse：重复时，每次都从结束值开始。
            initialStartOffset: StartOffset = StartOffset(0)：设置初始的延迟。有两种模式：
                StartOffsetType.Delay：延迟多少毫秒后开始动画。
                StartOffsetType.FastForward：快进到动画的某个时刻开始动画。即掐掉动画的前面一段时间，从某个时刻开始动画。
     */

    Column {
        Box1()
        Spacer(modifier = Modifier.height(30.dp))
        Box2()
        Spacer(modifier = Modifier.height(30.dp))
        Box3()
        Spacer(modifier = Modifier.height(30.dp))
        Box4()
    }
}


@Composable
private fun Box1() {
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
        sizeAnimatable.animateTo(
            size,
            repeatable(
                iterations = 4,
                // 偶数次重复 + 倒放模式会出现问题【因为动画的结束值和目标值不同了，这也与 animateTo 方法的定义相违背】
                repeatMode = RepeatMode.Reverse,
                animation = TweenSpec(easing = LinearEasing),
            )
        )
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}


@Composable
private fun Box2() {
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
        sizeAnimatable.animateTo(
            size,
            repeatable(
                iterations = 3,
                repeatMode = RepeatMode.Reverse,
                animation = TweenSpec(easing = LinearEasing),
            )
        )
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Green)
            .clickable {
                big = !big
            })
}


@Composable
private fun Box3() {
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
        sizeAnimatable.animateTo(
            size,
            repeatable(
                iterations = 3,
                repeatMode = RepeatMode.Reverse,
                animation = TweenSpec(easing = LinearEasing),
                initialStartOffset = StartOffset(1000)
            )
        )
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Blue)
            .clickable {
                big = !big
            })
}

@Composable
private fun Box4() {
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
        sizeAnimatable.animateTo(
            size,
            repeatable(
                iterations = 2,
                repeatMode = RepeatMode.Reverse,
                animation = TweenSpec(easing = LinearEasing),
                initialStartOffset = StartOffset(500, StartOffsetType.FastForward)
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