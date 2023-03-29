package me.ztiany.compose.foundation.gesture.pointerinput.foreach

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import timber.log.Timber


/** 演示 awaitDragOrCancellation 的使用 */
@Composable
fun AwaitDragOrCancellationViews() {
    /*
    与 drag 不同的是，awaitDragOrCancellation 负责监听单次拖动事件。当该手指抬起时，如果有其他手指还在屏幕上，则会选择其中一根手指来继续追踪手势。当最后一根手指离
    开屏幕时，则会返回抬起事件。

    当手指拖动事件已经在 Main 阶段被消费，拖动行为会被认为已经取消，此时会返回 null。如果在调用 awaitDragOrCancellation 前，pointId 对应手指没有产生 ACTION_DOWN 事件，
    则也会返回 null。当然也可以使用 awaitDragOrCancellation 来完成 UI 拖动手势处理流程。
     */
    val boxSize = 100.dp
    var offsetState by remember {
        mutableStateOf(Offset(0F, 0F))
    }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        var downPointer = awaitFirstDown().id
                        Timber.d("downPointer = $downPointer")
                        while (true) {
                            val event = awaitDragOrCancellation(downPointer) ?: /*拖动被取消*/break
                            Timber.d("event.id = ${event.id}")
                            if (event.changedToUpIgnoreConsumed()) {
                                //所有手指抬起
                                break
                            }
                            offsetState += event.positionChange()
                            downPointer = event.id
                        }
                    }
                }
            }) {
        Box(
            Modifier
                .size(boxSize)
                .offset {
                    IntOffset(offsetState.x.toInt(), offsetState.y.toInt())
                }
                .background(Color.Red)
        )
    }
}