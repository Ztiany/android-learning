package me.ztiany.compose.rwx.version1

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
    1. 声明式 UI 直接操作属性，而不是拿到对象来操作。
    2. 我们对于动画过程的设置也是隐式的。
 */
@Composable
fun Lesson08() {
    Column(Modifier.verticalScroll(rememberScrollState(0))) {
        AnimateAsStateBox()
        AnimatableBox()
        TransitionBox()
        ConvenientBox()
    }
}

@Composable
private fun ConvenientBox() {
/*
    其他方便的 API：
        Modifier.animateContentSize()
        Crossfade
 */
}

/*
    Transition 用于多属性值的变化。
 */
@Preview
@Composable
private fun TransitionBox() {
    var big by remember {
        mutableStateOf(false)
    }

    val bigTransition = updateTransition(big, "big")

    val size by bigTransition.animateDp(label = "size") {
        if (it) {
            96.dp
        } else {
            48.dp
        }
    }

    val cornerSize by bigTransition.animateDp(label = "corner") {
        if (it) {
            16.dp
        } else {
            0.dp
        }
    }

    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(cornerSize))
            .background(Color.Green)
            .clickable {
                big = !big
            }
    ) {

    }
}

/*
    Animatable 可以设置起始值,可操控性更高。
 */
@Composable
private fun AnimatableBox() {
    val anim = remember {
        Animatable(48.dp, Dp.VectorConverter)
    }

    var big by remember {
        mutableStateOf(false)
    }

    //Compose 应该这样使用协程
    LaunchedEffect(big) {
        //瞬间变化
        anim.snapTo(if (big) 144.dp else 0.dp)

        //tween 用于设置时长。
        //anim.animateTo(if (big) 96.dp else 48.dp, tween(3000))

        //spring 用于设置弹簧，不支持时长，因为弹簧是基于物理模型推算的。
        anim.animateTo(if (big) 96.dp else 48.dp, spring(Spring.DampingRatioHighBouncy))
    }
    Box(
        Modifier
            .size(anim.value)
            .background(Color.Blue)
            .clickable {
                big = !big
            }
    ) {

    }
}

/*
    animateDpAsState 是对 Animatable 的简易封装【基于状态切换的简单动画用 animateXXXAsState】。
 */
@Composable
private fun AnimateAsStateBox() {
    var big by remember {
        mutableStateOf(false)
    }

    //animateDpAsState 返回的就是 State<T>，而且是自带 remember 功能的。
    val size by animateDpAsState(if (big) 96.dp else 48.dp)

    Box(
        Modifier
            .size(size)
            .background(Color.Green)
            .clickable {
                big = !big
            }
    ) {

    }
}