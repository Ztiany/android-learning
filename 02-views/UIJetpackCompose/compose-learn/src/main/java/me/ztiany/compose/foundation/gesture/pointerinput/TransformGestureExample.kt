package me.ztiany.compose.foundation.gesture.pointerinput

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/** 演示 detectTransformGestures 的使用*/
@Composable
fun TransformGestureViews() {
    val boxSize = 100.dp
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }

    /*
    使用 detectTransformGestures 可以获取到双指拖动、缩放与旋转手势操作中更具体的手势信息，与 Transformable 修饰符一样，detectTransformGestures 方法提供了两个参数。

                • panZoomLock（可选）：当拖动或缩放手势发生时是否支持旋转。
                • onGesture（必须）：当拖动、缩放或旋转手势发生时回调。

    注意：当我们处理旋转、缩放与拖动这类手势时，需要格外注意 Modifier 调用次序，因为这会影响最终呈现效果。
     */
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(Modifier
            .size(boxSize)
            .rotate(rotationAngle) // 需要注意 offset 与 rotate、scale 调用先后顺序
            .scale(scale)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
            .background(Color.Green)
            .pointerInput(Unit) {
                detectTransformGestures(panZoomLock = false, onGesture = { centroid: Offset, pan: Offset, zoom: Float, rotation: Float ->
                    offset += pan
                    scale *= zoom
                    rotationAngle += rotation
                })
            })
    }
}