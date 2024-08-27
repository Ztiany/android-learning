package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/*
 API: animateXxxAsState，定义在 androidx/compose/animation/core/AnimateAsState.kt
 用途：状态转移型动画
 */
@Composable
fun S401_AnimateXxxAsState() {
    var big by remember {
        mutableStateOf(false)
    }

    /*
        说明：
            animateXxxAsState 内部是以协程的方式来渐变地修改值。
            target 值既是初始值也是目标值，通过一个 state 变量来控制。
            animateXxxAsState 不适用与初始值与动画的起始站和目标值不一致的场景。比如初始值是 10，开启动画后需要里面从 30 变到 40 的需求，虽然这种场景很少见。
            animateXxxAsState 内部使用的是 Animatable，animateXxxAsState 针对状态转移做的封装，牺牲了灵活性，但是增强了便用性。
     */
    val size by animateDpAsState(if (big) 96.dp else 48.dp, label = "Size of Box")

    Box(
        Modifier
            .size(size)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}