package me.ztiany.compose.learn.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

private enum class State {
    Closed, Opened
}

/** 演示 Swipeable 的使用*/
@Composable
fun SwipeableViews() {
    Box(Modifier.fillMaxSize()) {
        SwipeableSwitch(Modifier.align(Alignment.Center))
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun SwipeableSwitch(modifier: Modifier = Modifier) {
    /*
    与 Draggable 修饰符不同的是，Swipeable 修饰符允许开发者通过锚点设置，为组件增加位置吸附交互效果，常用于开关、下拉刷新等。

    与 Draggable 修饰符一样，Swipeable 修饰符只能监听水平或垂直方向的手势事件，并且不会为被修饰组件提供任何默认动画，只能提供手势的偏移量信息，可依照自身需求来定制交互效果。

    使用 Swipeable 修饰符至少需要传入 4 个参数 State、Anchors、Orientation、Thresholds：

                • State：手势状态，通过状态可实时获取当前手势的偏移信息。
                • Anchors：锚点，用于记录不同状态对应数值的映射关系【其实就是听译不同状态对应的位置】。
                • Orientation：手势方向，被修饰组件的手势方向只能是水平或垂直方向。
                • thresholds（可选）：不同锚点之间吸附的临界阈值，常用的阈值有 FixedThreshold(Dp) 和 FractionalThreshold(Float) 两种。

        下面使用 Swipeable 修饰符来完成一个简单开关。首先定义了两个枚举项用于描述开关的状态，并且对开关的尺寸进行设置。
     */
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
    }
}