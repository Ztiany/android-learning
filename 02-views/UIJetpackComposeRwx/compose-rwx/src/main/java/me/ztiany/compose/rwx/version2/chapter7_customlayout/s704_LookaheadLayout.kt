package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LookaheadLayout
import androidx.compose.ui.layout.LookaheadLayoutCoordinates
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import timber.log.Timber
import kotlin.math.max

/*
    LookaheadLayout 用于动画过渡，特别是共享元素的过渡。传统 View 系统的 Transition API 的功能在 Compose 中就是 LookaheadLayout 实现的。

    LookaheadLayout 是基于自己的测量结果来再次测量。但是 Compose 是禁止多次测量的。

    二次测量：
        1. Compose 限制某个组件只能测量一次，那是在使用 Layout 时，而使用 Modifier.layout 时，可以多次测量。
        2. 对于 LookaheadLayout，LookaheadLayout 比不同的 Layout，多了一个前瞻的测量。它内部的子以及子孙组件都会被测量两次。
            LookaheadLayout 内部可以给子组件添加一个 intermediateLayout 的
 */
@Composable
fun S704_LookaheadLayout() {
    Column(modifier = Modifier.fillMaxSize()) {
        Demo1()
        Demo2()
        Demo3()
        Demo4()
        Demo5()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Demo5() {
    var isTextHeight200DP by remember {
        mutableStateOf(false)
    }
    var textHeightPx by remember {
        mutableStateOf(0)
    }
    val animationHeight by animateIntAsState(textHeightPx, label = "textHeight")

    var lookaheadOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    val lookaheadAnimationOffset by animateOffsetAsState(lookaheadOffset, label = "textHeight")

    val sharedText = remember {
        movableContentWithReceiverOf<LookaheadLayoutScope>() {

            Text(text = "Hello",
                Modifier
                    .then(if (isTextHeight200DP) Modifier.padding(50.dp) else Modifier)
                    .onPlaced { lookaheadScopeCoordinates: LookaheadLayoutCoordinates, layoutCoordinates: LookaheadLayoutCoordinates ->
                        // 相对于 LookaheadLayout 的位置
                        lookaheadOffset = lookaheadScopeCoordinates.localLookaheadPositionOf(layoutCoordinates)
                        // 相对于下游 NodeCoordinator 的位置
                        lookaheadScopeCoordinates.localPositionOf(layoutCoordinates, Offset.Zero)
                    }
                    // 负责提供动画过程中的中间值。
                    .intermediateLayout { measurable, constraints, lookaheadSize ->
                        Timber.d("intermediateLayout, lookaheadSize: $lookaheadSize")
                        // 启动动画，提供目标值
                        textHeightPx = lookaheadSize.height

                        // 在动画过程中，设置从动画开始到结束中间的值。
                        val placeable = measurable.measure(
                            Constraints.fixed(
                                lookaheadSize.width,
                                // 这里读取了 animationHeight，animationHeight 修改就会导致 intermediateLayout 重新调用。
                                animationHeight
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                (lookaheadAnimationOffset.x - lookaheadOffset.x).toInt(),
                                (lookaheadAnimationOffset.y - lookaheadOffset.y).toInt()
                            )
                        }
                    }
                    .then(if (isTextHeight200DP) Modifier.height(200.dp) else Modifier)
                    .clickable {
                        isTextHeight200DP = !isTextHeight200DP
                    })
        }
    }

    SimpleLookaheadLayout {
        if (isTextHeight200DP) {
            sharedText()
        } else {
            Column {
                Text(text = "World")
                sharedText()
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Demo4() {
    var isTextHeight200DP by remember {
        mutableStateOf(false)
    }
    var textHeightPx by remember {
        mutableStateOf(0)
    }
    val animationHeight by animateIntAsState(textHeightPx, label = "textHeight")

    var lookaheadOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    val lookaheadAnimationOffset by animateOffsetAsState(lookaheadOffset, label = "textHeight")

    Column {
        SimpleLookaheadLayout {
            Text(text = "Hello",
                Modifier
                    .then(if (isTextHeight200DP) Modifier.padding(50.dp) else Modifier)
                    .onPlaced { lookaheadScopeCoordinates: LookaheadLayoutCoordinates, layoutCoordinates: LookaheadLayoutCoordinates ->
                        // 相对于 LookaheadLayout 的位置
                        lookaheadOffset = lookaheadScopeCoordinates.localLookaheadPositionOf(layoutCoordinates)
                        // 相对于下游 NodeCoordinator 的位置
                        lookaheadScopeCoordinates.localPositionOf(layoutCoordinates, Offset.Zero)
                    }
                    // 负责提供动画过程中的中间值。
                    .intermediateLayout { measurable, constraints, lookaheadSize ->
                        Timber.d("intermediateLayout, lookaheadSize: $lookaheadSize")
                        // 启动动画，提供目标值
                        textHeightPx = lookaheadSize.height

                        // 在动画过程中，设置从动画开始到结束中间的值。
                        val placeable = measurable.measure(
                            Constraints.fixed(
                                lookaheadSize.width,
                                // 这里读取了 animationHeight，animationHeight 修改就会导致 intermediateLayout 重新调用。
                                animationHeight
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                (lookaheadAnimationOffset.x - lookaheadOffset.x).toInt(),
                                (lookaheadAnimationOffset.y - lookaheadOffset.y).toInt()
                            )
                        }
                    }
                    .then(if (isTextHeight200DP) Modifier.height(200.dp) else Modifier)
                    .clickable {
                        isTextHeight200DP = !isTextHeight200DP
                    })
        }
        Text(text = "World")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Demo3() {
    var isTextHeight200DP by remember {
        mutableStateOf(false)
    }
    var textHeightPx by remember {
        mutableStateOf(0)
    }
    val animationHeight by animateIntAsState(textHeightPx, label = "textHeight")

    Column {
        SimpleLookaheadLayout {
            Text(text = "Hello",
                Modifier
                    // 负责提供动画过程中的中间值。
                    .intermediateLayout { measurable, constraints, lookaheadSize ->
                        Timber.d("intermediateLayout, lookaheadSize: $lookaheadSize")
                        // 启动动画，提供目标值
                        textHeightPx = lookaheadSize.height

                        // 在动画过程中，设置从动画开始到结束中间的值。
                        val placeable = measurable.measure(
                            Constraints.fixed(
                                lookaheadSize.width,
                                // 这里读取了 animationHeight，animationHeight 修改就会导致 intermediateLayout 重新调用。
                                animationHeight
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    // 虽然 SimpleLookaheadLayout 有两次测量，但是一般只有第一帧有两次测量，中间的帧只有一次测量。
                    // 即：第一帧的时候由于修改了 isTextHeight200DP，从而修改了高度值，因此测量结果发生变化，从而导致了第二次测量。
                    // 而之后的动画过程中，下面的 then 没有改变，因此就不会再次测量。也就是在动画过程中，只有 intermediateLayout 会在每一帧调用。
                    //.layout(measurable, constraints) { }
                    .then(
                        if (isTextHeight200DP) Modifier.height(200.dp) else Modifier
                    )
                    .clickable {
                        isTextHeight200DP = !isTextHeight200DP
                    })
        }
        Text(text = "World")
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun SimpleLookaheadLayout(
    content: @Composable @UiComposable LookaheadLayoutScope.() -> Unit
) {
    LookaheadLayout(content = content, measurePolicy = { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val width = placeables.maxOf { it.width }
        val height = placeables.sumOf { it.height }
        layout(width, height) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    })
}

@Composable
private fun Demo2() {
    // 如何实现基于 Text 自身的高度来实现动画？而不是预先就写死一个 100 dp 的高度。
    var textHeight by remember {
        mutableStateOf(100.dp)
    }
    val animation = animateDpAsState(textHeight, label = "textHeight")
    Column {
        Text(text = "Hello",
            Modifier
                .height(animation.value)
                .onSizeChanged {
                    // 也不能使用 onSizeChanged，因为在 onSizeChanged 中修改了高度状态，会再重组，而重组又会再次调用 onSizeChanged，这样就会导致死循环。
                }
                .clickable {
                    textHeight = if (textHeight == 100.dp) 200.dp else 100.dp
                })
        Text(text = "World")
    }

    // 此时，就需要用到 LookaheadLayout。参考 Demo3
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Demo1() {
    // 目的不在于二次测量，而在于实现过渡动画的效果。
    LookaheadLayout(content = {
        Column {
            Text(text = "Hello", modifier = Modifier
                //.layout { measurable, constraints -> } // 两次都会被调用
                // intermediateLayout 在第二次正式测量才会被调用，它可以拿到它右（下）边的 layout 的测量结果。
                .intermediateLayout { measurable, constraints, lookaheadSize ->
                    Timber.d("intermediateLayout")
                    val placeable = measurable.measure(
                        Constraints.fixed(
                            lookaheadSize.width,
                            lookaheadSize.height * 2
                        )
                    )
                    layout(placeable.width + lookaheadSize.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .layout { measurable, constraints ->// 两次都会被调用
                    Timber.d("layout")
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                })

            Text(text = "World")
        }
    }, measurePolicy = { measurables, constraints ->
        var width = 0
        var height = 0
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints).also { placeable ->
                width = max(width, placeable.width)
                height += placeable.height
            }
        }
        layout(width, height) {
            var totalHeight = 0
            placeables.forEach {
                it.placeRelative(0, totalHeight)
                totalHeight += it.height
            }
        }
    })
}