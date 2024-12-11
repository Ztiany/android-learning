package me.ztiany.compose.learn.gesture.pointerinput

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/** 演示 detectDragGestures 的使用*/
@Composable
fun DetectDragGesturesViews() {
    /*
    Draggable 修饰符作为手势处理的高层次封装，在监听 UI 组件拖动手势的基础能力上也附加了许多特性与限制，同时也隐藏了一些细粒度的手势事件回调设置。例如在 Draggable 修饰符
    中只能监听水平或垂直两个方向的拖动手势，所以为了能够更完整地监听拖动手势，Compose 为我们提供了低级别的 detectDragGestures 系列 API。

                • detectDragGestures：监听任意方向的拖动手势。
                • detectDragGesturesAfterLongPress：监听长按后的拖动手势。
                • detectHorizontalDragGestures：监听水平拖动手势。
                • detectVerticalDragGestures：监听垂直拖动手势。

    这类拖动监听 API 功能上相类似，使用时需要传入的参数也比较相近。可以根据实际情况来选用不同的 API。在使用这些 API 时，可以定制在不同时机的处理回调。
    这里提供了 4 个回调时机：

                - onDragStart 会在拖动开始时回调
                - onDragEnd 会在拖动结束时回调
                - onDragCancel 会在拖动取消时回调
                - onDrag 则会在拖动真正发生时回调

    onDragCancel 触发时机多发生于滑动冲突的场景，子组件可能最开始是可以获取到拖动事件的，当拖动手势事件达到某个指定条件时，可能会被父组件劫持消费，这种场景下便会执行
    onDragCancel 回调。所以 onDragCancel 回调主要依赖于实际业务逻辑。
     */
    Column(Modifier.fillMaxSize()) {

        var resultState by remember {
            mutableStateOf("")
        }
        var resultDragging by remember {
            mutableStateOf("")
        }

        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Blue, RoundedCornerShape(10.dp))
                .height(100.dp)
                .fillMaxWidth()
                // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                        resultState = "onDragStart-${it}"
                    }, onDragCancel = {
                        resultState = "onDragCancel"
                    }, onDragEnd = {
                        resultState = "onDragEnd"
                    }, onDrag = { change, dragAmount ->
                        resultDragging = "onDrag-change = ${change}, dragAmount = $dragAmount"
                    })
                }) {
            Text(text = "操作我", Modifier.align(Alignment.Center))
        }

        Text(text = resultState)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = resultDragging)
    }
}
