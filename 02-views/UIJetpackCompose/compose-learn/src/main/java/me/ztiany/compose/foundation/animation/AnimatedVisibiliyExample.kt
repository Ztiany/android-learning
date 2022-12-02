package me.ztiany.compose.foundation.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** 演示 AnimatedVisibility 的使用*/
@Composable
fun AnimatedVisibilityText() {
    var visible by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {

        /*
        AnimatedVisibility 是一个容器类的 Composable，需要接收一个 Boolean 型的 visible 参数控制 content 是否可见，content 在出现与消失
        时，会伴随着过渡动画效果。

            （1）可以通过设置 EnterTransition 和 ExitTransition 来定制出场与离场过渡动画，当出场动画完成时，content 便会从视图树上移除。
            （2）默认情况下 EnterTransition 是 fadeIn+expandIn 的效果组合，ExitTransition 是 fadeOut+shrinkOut 的效果组合。

        Compose 额外提供了 RowScope.AnimatedVisibility 和 ColumnScope. AnimatedVisibility 两个扩展方法，我们可以在 Row 或者 Column 中调用
        AnimatedVisibility，该组件的默认过渡动画效果会根据父容器的布局特征进行调整，比如：

                - 在 Row 中默认 EnterTransition 是fadeIn+expandHorizontally组合方案；
                - 在 Column 中默认 EnterTransition 则是 fadeIn+expandVertically 组合方案。
        */
        AnimatedVisibility(visible = visible, enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ), exit = slideOutVertically() + shrinkVertically() + fadeOut()) {
            Text(
                text = "Hello", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Red)
            )
        }

        Button(onClick = { visible = !visible }) {
            Text(text = "Click to change")
        }
    }
}


/** 演示 AnimatedVisibility + MutableTransitionState 的使用*/
@Composable
fun AnimatedVisibilityTextStartWhenEnter() {
    val density = LocalDensity.current
    Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {

        /*
        AnimatedVisibility 还有一个接收 MutableTransitionState 类型参数的重载方法。

        MutableTransitionState 的关键成员有两个：当前状态 currentState 和目标状态 targetState。两个状态的不同驱动了动画的执行。
        */
        val state = remember {
            // 设置初始值为 false
            MutableTransitionState(false).apply {
                // 设置目标值为 true
                targetState = true
            }
        }

        /*
        当 AnimatedVisibility 上屏（即Composable组件的OnActive）时，由于 visibleState 的两个状态的不同，动画会立即执行。
        可以用类似的做法实现一些开屏时的动画。
        */
        AnimatedVisibility(
            visibleState = state,
            //进入动画
            enter = slideInVertically(
                // Slide in from 40 dp from the top.
                initialOffsetY = { with(density) { -40.dp.roundToPx() } },
                // 动画规格
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            ) + expandVertically(
                // Expand from the top.
                expandFrom = Alignment.Top,
                // 动画规格
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f,
                // 动画规格
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            ),
            //退出动画
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Text(
                // MutableTransitionState 的意义还在于通过 currentState 和 isIdle 的值，可以获取动画的执行状态。
                text = when {
                    state.isIdle && state.currentState -> "Visible"
                    !state.isIdle && state.currentState -> "Disappearing"
                    state.isIdle && !state.currentState -> "Invisible"
                    else -> "Appearing"
                }, textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Red)
            )
        }

        Button(onClick = { state.targetState = !state.targetState }) {
            Text(text = "Click to change")
        }
    }

}


/** 演示 AnimatedVisibility + animateEnterExit 的使用*/
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilitySpecialChildren() {
    var visible by remember { mutableStateOf(true) }

    Column {

        Button(onClick = { visible = !visible }) {
            Text(text = "Click to change")
        }

        AnimatedVisibility(
            visible = visible, enter = fadeIn(), exit = fadeOut()
        ) {
            // Fade in/out the background and the foreground.
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        /*
                        在 AnimatedVisibility 的 content 中，可以使用 Modifier.animateEnterExit 为每个子元素单独设置进出屏幕的过渡动画。

                        比如这个例子中，后添加的 slide 动画会覆盖AnimatedVisibility 设置的 fade 动画。有时我们希望 AnimatedVisibility 内部每个
                        子组件的过渡动画各不相同，此时可以为 AnimatedVisibility 的 enter 与 exit 参数分别设置 EnterTransition. None 和
                        ExitTransition. None，并在每个子组件分别指定 animateEnterExit 就可以了。
                        */
                        .animateEnterExit(
                            // Slide in/out the inner box.
                            enter = slideInVertically(), exit = slideOutVertically()
                        )
                        .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
                        .background(Color.Red)
                ) {
                    // Content of the notification…
                }
            }
        }
    }

}

/** 演示 AnimatedVisibility + animateEnterExit 的使用*/
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityCustomized() {
    var visible by remember { mutableStateOf(true) }

    Column {

        Button(onClick = { visible = !visible }) {
            Text(text = "Click to change")
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
        ) { // this: AnimatedVisibilityScope
            // Use AnimatedVisibilityScope#transition to add a custom animation
            // to the AnimatedVisibility.
            val background by transition.animateColor(label = "BoxBG") { state ->
                if (state == EnterExitState.Visible) Color.Blue else Color.Gray
            }
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(background)
            )
        }
    }
}