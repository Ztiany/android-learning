package me.ztiany.compose.foundation.gesture.pointerinput.foreach

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import timber.log.Timber


/** 演示 drag 的使用 */
@Composable
fun DragViews() {
    /*
    前面提到的 detectDragGestures，以及更为上层的 Draggable 修饰符内部都是使用 drag 挂起方法来实现拖动监听的。通过函数签名可以看到不仅需要手指拖动的监听回调，还需传入手指
    的标识信息，表示监听具体哪根手指的拖动手势。

    可以先利用 awaitFirstDown 获取到记录着交互信息的 PointerInputChange 实例，其中 id 字段记录着发生 ACTION_DOWN 事件的手指标识信息。通过结合 forEachGesture、
    awaitFirstDown 与 drag，可以实现一个简单的拖动手势监听。
     */
    val boxSize = 100.dp
    var offsetState by remember {
        mutableStateOf(IntOffset(10, 10))
    }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .size(boxSize)
                .offset {
                    offsetState
                }
                .background(Color.Red)
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val id = awaitFirstDown().id
                            drag(id) {
                                   Timber.d("now ${it.position} previous ${it.previousPosition}")
                                offsetState = IntOffset(
                                    offsetState.x + (it.position.x - it.previousPosition.x).toInt(),
                                    offsetState.y + (it.position.y - it.previousPosition.y).toInt(),
                                )
                            }
                        }
                    }
                }
        )
    }
}
