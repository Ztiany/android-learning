package me.ztiany.compose.learn.gesture

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


/** 演示 Swipeable 的使用 */
@Composable
fun SwipeableViews() {
    Box(Modifier.fillMaxSize()) {
        AnchoredSwipeableSwitch(Modifier.align(Alignment.Center))
    }
}

private enum class State {
    Closed,
    Opened
}

/**
 * see:
 *
 *  - [Migrate from Swipeable to AnchoredDraggable](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/migrate-swipeable)
 *  - [Exploring Jetpack Compose Anchored Draggable Modifier](https://fvilarino.medium.com/exploring-jetpack-compose-anchored-draggable-modifier-5fdb21a0c64c)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnchoredSwipeableSwitch(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val blockSize = 48.dp
    val blockSizePx = with(LocalDensity.current) { blockSize.toPx() }
    val anchors = DraggableAnchors {
        State.Closed at 0F
        State.Opened at blockSizePx * 2
    }

    val swipeableState = remember {
        AnchoredDraggableState(
            initialValue = State.Opened,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.5F },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    Box(
        modifier = modifier
            .width(blockSize * 3)
            .height(blockSize)
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                // 由于 Modifier 链式执行的特性，此时 offset 修饰符必须先于 background 修饰符与 anchoredDraggable 修饰符。
                .offset {
                    IntOffset(
                        swipeableState
                            .requireOffset()
                            .roundToInt(),
                        0
                    )
                }
                .anchoredDraggable(
                    state = swipeableState,
                    orientation = Orientation.Horizontal
                )
                .size(blockSize)
                .background(Color.Blue)
        ) {
            Text(text = "拖", Modifier.align(Alignment.Center), color = Color.White)
        }
    }
}

/**
 * Note: Swipeable has been removed in M3.
 */
@Composable
private fun SwipeableSwitch(modifier: Modifier = Modifier) {
    /*
        与 Draggable 修饰符不同的是，Swipeable 修饰符允许开发者通过锚点设置，为组件增加位置吸附交互效果，常用于开关、下拉刷新等。
        与 Draggable 修饰符一样，Swipeable 修饰符只能监听水平或垂直方向的手势事件，并且不会为被修饰组件提供任何默认动画，只能提供手势的偏移量信息，组件可依照自身需求来定制交互效果。
        使用 Swipeable 修饰符至少需要传入 4 个参数 State、Anchors、Orientation、Thresholds：
                • State：手势状态，通过状态可实时获取当前手势的偏移信息。
                • Anchors：锚点，用于记录不同状态对应数值的映射关系【其实就是定义不同状态对应的位置】。
                • Orientation：手势方向，被修饰组件的手势方向只能是水平或垂直方向。
                • thresholds（可选）：不同锚点之间吸附的临界阈值，常用的阈值有 FixedThreshold(Dp) 和 FractionalThreshold(Float) 两种。
        下面使用 Swipeable 修饰符来完成一个简单开关。首先定义了两个枚举项用于描述开关的状态，并且对开关的尺寸进行设置。
     */

    /*
    val blockSize = 48.dp
    val blockSizePx = with(LocalDensity.current) { blockSize.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = State.Closed)
    val anchors = mapOf(
        0F to State.Closed,
        blockSizePx * 2 to State.Opened
    )

    Box(
        modifier = modifier
            .width(blockSize * 3)
            .height(blockSize)
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                //由于 Modifier 链式执行的特性，此时 offset 修饰符必须先于 background 修饰符与 swipeable 修饰符。
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    //希望当从关闭状态拖动到开启状态，滑块仅需移动超过 30% 距离时，则会自动吸附到开启状态。
                    // 而当我们从开启状态到关闭状态时，滑块需移动超过 50% 才会自动吸附到关闭状态
                    thresholds = { from, _ ->
                        if (from == State.Closed)
                            FractionalThreshold(0.3F)
                        else
                            FractionalThreshold(0.5F)
                    },
                    orientation = Orientation.Horizontal
                )
                .size(blockSize)
                .background(Color.DarkGray)
        ) {
            Text(text = "拖", Modifier.align(Alignment.Center))
        }
    }*/
}