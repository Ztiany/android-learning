package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import timber.log.Timber

@Composable
fun S705_TouchHandling() {
    Column(Modifier.fillMaxSize()) {
        Demo1()
        Demo2()
        Demo3()
    }
}

@Composable
private fun Demo3() {
    /*
        Modifier.swipelable() 在 material3 中被隐藏了。原因可能是因为 swiplable 太专业了。
        没有必要提供给开发者，而是提供基于它实现的控件。
    */
}

@Composable
private fun Demo2() {
    /*
    Modifier.scrollable() 基于 draggable 支持：
        - 惯性滑动
        - 嵌套滑动
        - 滑动触边效果（OverScroll）
     */
    val modifier = Modifier.scrollable(
        state = rememberScrollableState(consumeScrollDelta = {
            // 滑动了多少，返回消费的距离（嵌套滑动）
            Timber.d("consumeScrollDelta: $it")
            it
        }),
        orientation = Orientation.Vertical,
        enabled = true,
        reverseDirection = false,
        flingBehavior = null, // 惯性滑动的规则，null 表示使用默认规则
    )
}

@Composable
private fun Demo1() {
    /*
    Modifier.draggable 的参数：
        state: DraggableState, 获取拖动的状态，每种 API 的 State 都不同
        orientation: Orientation, 方向
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource? = null, 表示拖动状态的变化
        startDragImmediately: Boolean = false, 是否立即开始拖动，移动一定距离才认为是拖动
        onDragStarted: suspend CoroutineScope.(startedPosition: Offset) -> Unit = {},
        onDragStopped: suspend CoroutineScope.(velocity: Float) -> Unit = {},
        reverseDirection: Boolean = false，倒着拖动
    */

    val source = remember {
        MutableInteractionSource()
    }
    val offsetX = remember {
        mutableStateOf(0F)
    }
    val state = rememberDraggableState(onDelta = {
        offsetX.value += it
    })
    Column {
        Text(text = "Draggable", modifier = Modifier
            .offset { IntOffset(offsetX.value.toInt(), 0) }
            .draggable(
                state = state,
                orientation = Orientation.Horizontal,
                interactionSource = source,
            ))
        val collectIsDraggedAsState by source.collectIsDraggedAsState()
        Text(text = if (collectIsDraggedAsState) "Dragging" else "Not dragging")
    }
}