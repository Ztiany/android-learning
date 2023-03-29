package me.ztiany.compose.foundation.gesture.pointerinput

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


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
