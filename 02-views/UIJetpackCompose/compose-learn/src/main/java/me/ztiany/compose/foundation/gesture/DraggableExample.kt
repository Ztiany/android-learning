package me.ztiany.compose.foundation.gesture

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/** 演示 Draggable 的使用*/
@Composable
fun DraggableViews() {
    /*
    Draggable 修饰符允许开发者监听 UI 组件的拖动手势偏移量，根据偏移量定制 UI 拖动交互效果。值得注意的是，Draggable 修饰符只能监听垂直方向或水平方向的偏移。
    如果此时我们希望监听任意方向的偏移，则需要使用低级别 detectDragGestures 方法来实现。

    使用 Draggable 修饰符至少需要传入两个参数 draggableState、orientation。
                • draggableState：通过 draggableState，可以获取到拖动手势的偏移量，以此来动态控制发生偏移行为。
                • orientation：监听的拖动手势方向，只能是水平方向(Orientation. Horizontal)或垂直方向(Orientation. Vertical)。
     */
    var offsetX by remember {
        mutableStateOf(0F)
    }

    val initOffsetY = with(LocalDensity.current) { 20.dp.toPx() }
    var offsetY by remember {
        mutableStateOf(initOffsetY)
    }

    val draggableStateX = rememberDraggableState {
        offsetX += it
    }

    val draggableStateY = rememberDraggableState {
        offsetY += it
    }

    Box(
        Modifier
            .padding(20.dp)
            .fillMaxSize()
            .border(5.dp, Color.Red)
    ) {

        Box(
            Modifier
                .size(20.dp)
                //由于 Modifier 是链式执行的，所以此时 offset 修饰符需在 draggable 修饰符与 background 修饰符之前执行。
                .offset {
                    IntOffset(offsetX.toInt(), 0)
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = draggableStateX
                )
                .background(Color.Red)
        ) {

        }

        Box(
            Modifier
                .size(20.dp)
                .offset {
                    IntOffset(0, offsetY.toInt())
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = draggableStateY
                )
                .background(Color.Blue)
        ) {

        }
    }
}