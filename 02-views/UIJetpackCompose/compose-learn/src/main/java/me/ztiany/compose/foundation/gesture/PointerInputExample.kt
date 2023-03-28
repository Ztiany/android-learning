package me.ztiany.compose.foundation.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/*
Draggable 修饰符、Swipeable 修饰符、Transformable 修饰符以及 NestedScroll 修饰符能处理常见的手势交互，而针对复杂手势需求，我们需要对 Compose 中的手势处理有更深入的理解。
实际上前面所提到的手势处理修饰符都是基于低级别的 PointerInput 修饰符进行封装实现的，所以弄清楚 PointerInput 修饰符的使用方法，有助于对高级别手势处理修饰符的理解，并且能够
帮助我们更好地完成上层开发，实现各种复杂的手势需求。

使用 PointerInput 修饰符时，需要传入两个参数：keys 与 block。

            • keys：当 Composable 组件发生重组时，如果传入的 keys 发生了变化，则手势事件处理过程会被中断。
            • block：在这个 PointerInputScope 类型作用域代码块中，便可以声明手势事件处理逻辑了。通过 suspend 关键字可知这是一个协程体，意味着在 Compose 中手势处理最终都发生在协程中。

在 PointerInputScope 接口声明中能够找到所有可用的手势处理方法，可以通过这些方法获取到更加详细的手势信息，以及更加细粒度的手势事件处理，

下面示例用于演示 PointerInputScope 中的 GestureDetector 系列 API 的使用。
 */

/** 演示 detectTapGestures 的使用*/
@Composable
fun DetectTapGesturesViews() {
    /*
        在 PointerInputScope 中，可以使用 detectTapGestures 设置更细粒度的点击监听回调。作为低级别点击监听API，在发生点击时不会带有像 Clickable 修饰符与 CombinedClickable
        修饰符那样，为所修饰的组件施加一个涟漪波纹效果动画的蒙层。
     */
    Column(Modifier.fillMaxSize()) {

        var result by remember {
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
                    detectTapGestures(
                        //onPress 是最普通的 ACTION_DOWN 事件，手指一旦按下便会回调。
                        onPress = {
                            result = "onPress-${it}"
                        },
                        //如果手指按下后快速抬起，在轻触的判定阈值内(100ms)会执行onTap回调。
                        onTap = {
                            result = "onTap-${it}"
                        },
                        //如果连着按了两下，则会在执行两次 onPress 后执行 onDoubleTap。
                        onDoubleTap = {
                            result = "onDoubleTap-${it}"
                        },
                        //如果手指按下后不抬起，当达到长按的判定阈值(400ms)会执行 onLongPress。
                        onLongPress = {
                            result = "onLongPress-${it}"
                        },
                    )
                }) {
            Text(text = "操作我", Modifier.align(Alignment.Center))
        }

        Text(text = result)
    }

}

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

/** 演示 forEachGesture 的使用*/
@Composable
fun ForEachGestureViews() {
    /*
    forEachGesture 存在的意义：Compose 手势操作实际上是在协程中监听处理的，当协程处理完一次手势交互后便会结束（从 DOWN 到 UP），当进行第二次手势交互时由于负责手势
    监听的协程已经结束，手势事件便会被丢弃掉。那么怎样才能让手势监听协程不断地处理每一轮的手势交互呢？    我们很容易想到可以在外层嵌套一个 while(true) 进行实现，然而
    这么做并不优雅，且存在着一些问题。

            1. 当用户出现一连串手势操作时，很难保证各手势之间有清晰分界，即无法保证每一轮手势结束后，所有手指都是离开屏幕的（由于协程机制的导致，总之这在 Compose 中是可能发生的）。
            在传统 View 体系中，手指按下一次、移动到抬起过程中的所有手势事件可以看作是一个完整的手势交互序列。每当用户触摸屏幕交互时，可以根据这一次用户输入的手势交互序列中的信息
            进行相应的处理。
            2. 当第一轮手势处理结束或者被中断取消后，如果采用 while(true)，当第一轮手势因发生异常而中断处理时，此时手势仍在屏幕之上，则可能会影响第二轮手势处理，导致出现不
            符合预期的行为处理结果。
            3. Compose 为我们提供了 forEachGesture 方法，保证了每一轮手势处理逻辑的一致性。实际上上面的 GestureDetect 系列 API，其内部实现都使用了 forEachGesture。

通过 forEachGesture 的源码可知，每一轮手势处理结束后，或本次手势处理被取消时，都会使用 awaitAllPointersUp() 保证所有手指均已抬起，并且同时也会与当前组件的生命周期对齐，
当组件离开视图树时，手势监听也会随之结束。
     */

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = """
            forEachGesture 存在的意义：Compose 手势操作实际上是在协程中监听处理的，当协程处理完一次手势交互后便会结束（从 DOWN 到 UP），当进行第二次手势交互时由于负责手势监听的协程已经结束，    手势事件便会被丢弃掉。那么怎样才能让手势监听协程不断地处理每一轮的手势交互呢？    我们很容易想到可以在外层嵌套一个 while(true) 进行实现，然而这么做并不优雅，且存在着一些问题。
    
    
                1. 当用户出现一连串手势操作时，很难保证各手势之间有清晰分界，即无法保证每一轮手势结束后，所有手指都是离开屏幕的。在传统 View 体系中，手指按下一次、移动到抬起过程中的所有手势事件可以看作是一个完整的手势交互序列。每当用户触摸屏幕交互时，可以根据这一次用户输入的手势交互序列中的信息进行相应的处理。
                2. 当第一轮手势处理结束或者被中断取消后，如果采用 while(true)，当第一轮手势因发生异常而中断处理时，此时手势仍在屏幕之上，则可能会影响第二轮手势处理，导致出现不符合预期的行为处理结果。
                3. Compose 为我们提供了 forEachGesture 方法，保证了每一轮手势处理逻辑的一致性。实际上上面的 GestureDetect 系列 API，其内部实现都使用了 forEachGesture。


    通过 forEachGesture 的源码可知，每一轮手势处理结束后，或本次手势处理被取消时，都会使用 awaitAllPointersUp() 保证所有手指均已抬起，并且同时也会与当前组件的生命周期对齐，当组件离开视图树时，手势监听也会随之结束。
    
        """.trimIndent(), fontSize = 12.sp
        )
    }

}
