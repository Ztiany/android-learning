package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
Transition：
    原生 View 系统，表示 Activity 和 Fragment 的转场动画。
    Compose 系统，表示 Composable 之间的转场动画。利用 AndroidStudio 的 Compose Preview，我们可以预览这些转场动画，甚至可以查看每一时刻的动画效果。
 */
@Composable
fun S413_Transition() {
    Column {
        Box1()
        Box2()
        Box3()
    }
}

@Composable
private fun Box1() {
    var big by remember { mutableStateOf(false) }
    /*
    虽然 API 名叫 updateTransition，但是它还负责创建 Transition。Transition 内部会开启一个协程，利用这个协程来计算中间状态的值。

    这个 Transition 是用来管理状态的。相比于 animateXxxAsState，它是关注于状态，而不是具体的数值。它在一个协程内部计算多个状态切换的动画。

    Transition 站在一个更好的角度来管理动画，它可以管理多个动画，而 animateXxxAsState 只能管理一个动画。
     */
    val bigTransition = updateTransition(big, label = "big"/*label 参数用于预览界面的调试*/)

    /*
    animateDp 的参数：
        transitionSpec：通过一个 lambda 来提供 FiniteAnimationSpec，为什么不直接提供呢？ 因为这样更灵活，可以根据状态来提供不同的 FiniteAnimationSpec。
        label：用于预览界面的调试。
        targetValueByState：用于提供不同状态下的目标值。
     */
    val size by bigTransition.animateDp(label = "size",
        transitionSpec = {
            if (false isTransitioningTo true) {
                spring(dampingRatio = 0.5F)
            } else {
                tween()
            }
        }
    ) { isBig ->
        if (isBig) {
            126.dp
        } else {
            58.dp
        }
    }
    val corner by bigTransition.animateDp(label = "corner") { isBig ->
        if (isBig) {
            10.dp
        } else {
            50.dp
        }
    }

    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(Color.Red)
            .clickable {
                big = !big
            })
}

@Composable
private fun Box2() {
    var big by remember { mutableStateOf(false) }

    val bigState = remember { MutableTransitionState(!big) }
    bigState.targetState = big

    /*
    Compared to the updateTransition variant that takes a targetState, this function supports a
    different initial state than the first targetState.
     */
    val bigTransition = updateTransition(bigState, label = "big"/*label 参数用于预览界面的调试*/)

    val size by bigTransition.animateDp(label = "size",
        transitionSpec = {
            if (false isTransitioningTo true) {
                spring(dampingRatio = 0.5F)
            } else {
                tween()
            }
        }
    ) { isBig ->
        if (isBig) {
            126.dp
        } else {
            58.dp
        }
    }
    val corner by bigTransition.animateDp(label = "corner") { isBig ->
        if (isBig) {
            10.dp
        } else {
            50.dp
        }
    }

    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(Color.Red)
            .clickable {
                big = !big
            })
}


@Composable
private fun Box3() {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite")
    Text(text = "rememberInfiniteTransition")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    S413_Transition()
}
