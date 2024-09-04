package me.ztiany.compose.learn.gesture.highlevel

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import timber.log.Timber
import kotlin.math.roundToInt


/** 演示 Transformable 的使用*/
@Composable
fun TransformableViews() {
    /*
    双指拖动、缩放与旋转手势在日常开发中十分常见，常用于图片阅览编辑等需求场景。Transformable 修饰符可以使开发者十分轻松地监听组件的双指拖动、缩放或旋转手势事件，
    通过定制 UI 动画实现完整的交互效果。
     */
    val boxSize = 200.dp
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotationAngle by remember { mutableStateOf(45F) }
    var scale by remember { mutableStateOf(1F) }

    /*
    可以使用 rememberTransformableState 来创建一个 transformableState 状态传入 Transformable 修饰符中。

        在使用 rememberTransformableState 创建 transformableState 时，我们便可以设置双指拖动、缩放与旋转的手势监听回调了，可以根据回调信息来更新状态，从而影响 UI 的绘制。
     */
    val transformableState = rememberTransformableState { zoomChange: Float, panChange: Offset, rotationChange: Float ->
        scale *= zoomChange
        offset += panChange
        rotationAngle += rotationChange
        Timber.tag("TransformableViews").d("zoomChange: %f, panChange: %s, rotationChange: %f", zoomChange, panChange.toString(), rotationChange)
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Timber.tag("TransformableViews").d("TransformableViews.Box 1")
        Box(
            Modifier
                .size(boxSize)
                // 注意：一般情况下我们都需要组件在旋转后，当出现双指拖动时组件会跟随手指发生偏移。若 offset 在 rotate 之前调用，则会出现组件旋转后，当双指拖动时组件会以当前
                // 旋转角度为基本坐标轴进行偏移。这是由于当你先进行 offset ，则说明已经发生了偏移，而 rotate 时会改变当前 UI 组件整个坐标轴，所以出现与预期不符的情况出现。
                .rotate(rotationAngle)
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .scale(scale)
                .background(Color.Green)
                .transformable(
                    state = transformableState,
                    // 当 lockRotationOnZoomPan 为 true 时，在发生双指拖动或缩放时，不会同时监听用户的旋转手势信息。
                    lockRotationOnZoomPan = false
                )
        ) {
            Timber.tag("TransformableViews").d("TransformableViews.Box 2")
        }

    }

}