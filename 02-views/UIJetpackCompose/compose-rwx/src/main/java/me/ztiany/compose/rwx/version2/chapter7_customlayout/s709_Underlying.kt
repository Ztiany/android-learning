package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun S709_Underlying() {
    Text("rengwuxian", Modifier.rengClick {
        println("发生事件：rengClick")
    })
}

@Composable
private fun Modifier.rengClick(onClick: () -> Unit) = pointerInput(Unit) {
    awaitEachGesture {
        val down = awaitFirstDown()
        /*val event1 = awaitPointerEvent(PointerEventPass.Initial)
        val event2 = awaitPointerEvent(PointerEventPass.Main)
        val event3 = awaitPointerEvent(PointerEventPass.Final)
        event2.changes[0].isConsumed
        event2.changes[0].consume()
        event2.changes[0].isConsumed*/
        val event1 = awaitPointerEvent()
        val offset = event1.calculatePan()
        val scale = event1.calculateZoom()
        while (true) {
            val event = awaitPointerEvent()
            if (event.type == PointerEventType.Move) {
                val pos = event.changes[0].position
                if (pos.x < 0 || pos.x > size.width || pos.y < 0 || pos.y > size.height) {
                    break
                }
            } else if (event.type == PointerEventType.Release && event.changes.size == 1) {
                onClick()
                break
            }
        }
    }
}
