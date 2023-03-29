package me.ztiany.compose.foundation.gesture.pointerinput.foreach

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import timber.log.Timber

/*
其实 GestureDetector 系列 API 本质上仍然是一种封装，既然手势处理是在协程中完成的，那么手势监听自然是通过协程的挂起恢复实现的，这取代了传统的回调监听方式。
要想深入理解 Compose 手势处理，就需要学习更为底层的挂起处理方法。

PointerInputScope 允许我们通过使用 awaitPointerEventScope 方法获得 AwaitPointerEventScope 作用域，在 AwaitPointerEventScope 作用域中，可以使用 Compose 中
所有低级别的手势处理挂起方法。当 awaitPointerEventScope 内所有手势事件都处理完成后，awaitPointerEventScope 便会恢复执行将 Lambda 中最后一行表达式的数值作
为返回值返回。
 */

/** 演示 awaitPointerEvent 的使用*/
@Composable
fun AwaitPointerEventViews() {
    /*
    awaitPointerEvent 简介：
        awaitPointerEvent 可以称为”事件之源“，之所以称这个 API 为事件之源，是因为上层所有手势监听 API 都是基于它实现的，它的作用类似于传统 View 中的 onTouchEvent()。
        无论用户是按下、移动或抬起，都将视作一次手势事件，当手势事件发生时，awaitPointerEvent 会返回当前监听到的所有手势交互信息。



    事件分发顺序：
        另外，awaitPointerEvent 也跟事件分发与事件消费有直接关系，awaitPointerEvent 存在着一个可选参数 PointerEventPass，这个参数实际上是用来定制手势事件分发顺序的。
        PointerEventPass 有 3 个枚举值，可以让我们来决定手势的处理阶段。在 Compose 中，手势处理共有 3 个阶段：

                        • Initial 阶段：自上而下的分发手势事件。
                        • Main 阶段：自下而上地分发手势事件。
                        • Final 阶段：自上而下的分发手势事件。

                在 Initial 阶段，手势事件会在所有使用 Initial 参数的组件间自上而下地完成首次分发。利用 Initial 可以使父组件能够预先劫持消费手势事件，这类似于传统 View 中 onInterceptTouchEvent
                的作用。

                在 Main 阶段，手势事件会在所有使用 Main 参数的组件间自下而上地完成第二次分发。利用 Main 可以使子组件能先于父组件完成手势事件的处理，这有些类似于传统 View 中 onTouchEvent
                的作用。

                在 Final 阶段，手势事件会在所有使用 Final 参数的组件间自上而下地完成最后一次分发。Final 阶段一般用来让组件了解经历过前面几个阶段后的手势事件消费情况，从而确定自身行为。
                例如按钮组件可以不用将手指从按钮上移动开的事件而取消触摸反馈，因为这个事件可能已被父组件滚动器用于滚动消费了。



    事件的消费：

        awaitPointerEvent 返回了一个 PointerEvent 实例。从 PointerEvent 类的声明中可以看到包含了两个属性：changes 与 motionEvent。

                • motionEvent：实际上就是传统 View 系统中的 MotionEvent，由于被声明 internal，说明官方并不希望我们直接拿来使用。
                • changes：其中包含了一次手势交互中所有手指的交互信息。在多指操作时，利用 changes 可以轻松定制多指手势处理。

        单指交互的完整信息被封装在了一个 PointerInputChange 实例中，PointerInputChange 提供了以下手势信息。

                    val id: PointerId, 手指标识，可以根据这个标识跟踪一次完整的手势交互
                    val uptimeMillis: Long, 手势事件时间戳
                    val position: Offset, 手指相对组件上的位置
                    val pressed: Boolean, 是否为 ACTION_DOWN
                    val previousUptimeMillis: Long, 上一次手势事件时间戳
                    val previousPosition: Offset, 上一次手指相对组件上的位置
                    val previousPressed: Boolean, 上一次是否为 ACTION_DOWN
                    isInitiallyConsumed: Boolean, 该事件是否已经被消费
                    val type: PointerType = PointerType.Touch, 输入类型，比如鼠标、手指、手写笔以及橡皮等
                    val scrollDelta: Offset = Offset.Zero

        利用这些丰富的手势信息，可以在上层定制实现各类复杂的交互手势。可以看到其中的 consumed 成员记录着该事件是否已被消费，可以使用 PointerInputChange
        提供的 consume 系列 API 来修改这个事件的消费标记。

                changedToDown：是否已经按下，按下手势已消费则返回 false。
                changedToDownIgnoreConsumed：是否已经按下，忽略按下手势已消费标记。
                changedToUp：是否已经抬起，按下手势已消费则返回 false。
                changedToUpIgnoreConsumed：是否已经抬起，忽略按下手势已消费标记。

                positionChanged：位置是否发生了改变，移动手势已消费则返回 false。
                positionChangedIgnoreConsumed：位置发生了改变，略已消费标记。
                positionChange：位置改变量，移动手势已消费则返回 Offset.Zero。
                positionChangeIgnoreConsumed：位置改变量 ，忽略移动手势已消费标记。

                positionChangeConsumed：当前移动手势是否已被消费。
                anyChangeConsumed：当前按下手势或移动手势是否被消费。

                consumeDownChange：消费按下手势。
                consumePositionChange：消费移动手势。
                consumeAllChanges：消费按下与移动手势。

                isOutOfBounds：当前手势是否在固定范围内。

    可以通过设置 PointerEventPass 来定制嵌套组件间手势事件分发顺序。假设分发流程中组件 A 预先获取到了手势信息并进行消费，手势事件仍然会被之后的组件 B 获取到。
    组件 B 在使用 positionChange 获取的偏移值时会返回 Offset.ZERO，这是因为此时该手势事件已被标记为已消费的状态。当然组件 B 也可以通过 IgnoreConsumed 系列 API
    突破已消费标记的限制获取到手势信息。
     */
    Column(Modifier.fillMaxSize()) {

        //第一层
        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Blue, RoundedCornerShape(10.dp))
                .size(250.dp)
                // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitPointerEvent(PointerEventPass.Main)
                            Timber.d("layer 1")
                        }
                    }
                }) {


            //第二层
            Box(
                Modifier
                    .padding(20.dp)
                    .background(Color.Red, RoundedCornerShape(10.dp))
                    .size(200.dp)
                    .align(Alignment.Center)
                    // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                    .pointerInput(Unit) {
                        forEachGesture {
                            awaitPointerEventScope {
                                awaitPointerEvent(PointerEventPass.Main)
                                Timber.d("layer 2")
                            }
                        }
                    }) {


                //第三层
                Box(
                    Modifier
                        .padding(20.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp))
                        .size(150.dp)
                        .align(Alignment.Center)
                        // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                        .pointerInput(Unit) {
                            forEachGesture {
                                awaitPointerEventScope {
                                    awaitPointerEvent(PointerEventPass.Main)
                                    Timber.d("layer 3")
                                }
                            }
                        }) {


                    //第四层
                    Box(
                        Modifier
                            .padding(20.dp)
                            .background(Color.Green, RoundedCornerShape(10.dp))
                            .size(100.dp)
                            .align(Alignment.Center)
                            // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                            .pointerInput(Unit) {
                                forEachGesture {
                                    awaitPointerEventScope {
                                        awaitPointerEvent(PointerEventPass.Main).changes[0].consumeDownChange()
                                        Timber.d("layer 4")
                                    }
                                }
                            }) {


                    }

                }

            }

        }

    }

}