package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
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
    DurationBasedAnimationSpec 表示的动画都是时间确定的，而 SpringSpec（弹簧） 表示的动画是时间不确定的，它是基于物理模型的动画。
 */
@Composable
fun S406_AnimationSpec_SpringSpec() {
    /*
        spring 函数有三个参数：
            dampingRatio: Float = Spring.DampingRatioNoBouncy：阻尼比，设置弹簧有多弹，越大越不弹。Spring.DampingRatioNoBouncy 字面意思就是没有弹性，也就是不弹，它的值其实就是 1。
                注意：物理上，如果阻尼比是 0，那么弹簧会无限弹下去，但是 Compose 团队做了限制，如果阻尼比是 0，将没有动画效果。
            stiffness: Float = Spring.StiffnessMedium：刚度，设置弹簧的刚度，越大越硬，刚度越大，弹的速度就快。Spring.StiffnessMedium 字面意思就是中等刚度，它的值其实就是 1500。
            visibilityThreshold: T? = null：于弹簧模型无关，而是与动画的精确度有关，可见性阈值，其作用是，如果完全模拟物理上的弹簧，那么动画可能太久，有点浪费性能，所以 Compose 团队做了一个优化，当动画的值变化小于这个阈值时，就认为动画已经完成了，不再继续模拟物理上的弹簧。
     */

    Column {
        Box1()
        Spacer(modifier = Modifier.height(30.dp))
        Box2()
    }
}

@Composable
private fun Box2() {
    var big2 by remember {
        mutableStateOf(false)
    }

    val size2 = remember(big2) {
        if (big2) {
            126.dp
        } else {
            58.dp
        }
    }

    val sizeAnimatable2 = remember {
        Animatable(
            initialValue = size2, typeConverter = Dp.VectorConverter
        )
    }

    LaunchedEffect(big2) {
        // 起始值和结束值相同的情况，下面写死的是 58.dp，这是可以设置一个 initialVelocity，表示动画的初始速度，这样就可以实现弹簧振动的效果。
        sizeAnimatable2.animateTo(
            58.dp,
            spring(
                dampingRatio = 0.2f,
                stiffness = Spring.StiffnessLow,
            ), 2000.dp
        )
    }

    Box(
        Modifier
            .size(sizeAnimatable2.value)
            .background(Color.Green)
            .clickable {
                big2 = !big2
            })
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
        // 起始值和结束值不同的情况。
        sizeAnimatable.animateTo(
            size,
            spring(
                dampingRatio = 0.2f,
                stiffness = Spring.StiffnessLow,
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