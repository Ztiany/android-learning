package me.ztiany.compose.foundation.gesture

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "AwaitPointerEventScope"

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
        awaitPointerEvent 可以称为”事件之源“，之所以称这个API为事件之源，是因为上层所有手势监听 API 都是基于它实现的，它的作用类似于传统 View 中的 onTouchEvent()。
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
                positionChangedIgnoreConsumed：位置发生了改变(忽略已消费标记。
                positionChange：位置改变量，移动手势已消费则返回 Offset.Zero。
                positionChangeIgnoreConsumed：位置改变量 ，忽略移动手势已消费标记。

                positionChangeConsumed：当前移动手势是否已被消费。
                anyChangeConsumed：当前按下手势或移动手势是否被消费。

                consumeDownChange：消费按下手势。
                consumePositionChange：消费移动手势。
                consumeAllChanges：消费按下与移动手势。

                isOutOfBounds：当前手势是否在固定范围内。

    可以通过设置 PointerEventPass 来定制嵌套组件间手势事件分发顺序。假设分发流程中组件 A 预先获取到了手势信息并进行消费，手势事件仍然会被之后的组件 B 获取到。
    组件 B 在使用 positionChange 获取的偏移值时会返回 Offset.ZERO，这是因为此时该手势事件已被标记为已消费的状态。当然组件B也可以通过 IgnoreConsumed 系列 API
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
                            Log.d(TAG, "layer 1")
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
                                Log.d(TAG, "layer 2")
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
                                    Log.d(TAG, "layer 3")
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
                                        Log.d(TAG, "layer 4")
                                    }
                                }
                            }) {


                    }

                }

            }

        }

    }

}

/** 演示 awaitFirstDown 的使用 */
@Composable
fun AwaitFirstDownViews() {
    /* awaitFirstDown 将等待第一根手指 ACTION_DOWN 事件时恢复执行，并将手指按下事件返回。 */
    var result by remember {
        mutableStateOf("")
    }

    Column {
        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Green, RoundedCornerShape(10.dp))
                .size(250.dp)
                // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val awaitFirstDown = awaitFirstDown()
                            result = awaitFirstDown.position.toString()
                        }
                    }
                }) {

        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = result)
    }
}

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
                                Log.d(TAG, "now ${it.position} previous ${it.previousPosition}")
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
                        while (true) {
                            val event = awaitDragOrCancellation(downPointer) ?: /*拖动被取消*/break
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

/** 演示 awaitDragOrCancellation 的使用 */
@Composable
fun AwaitTouchSlopOrCancellationViews() {
/*
awaitTouchSlopOrCancellation 用于定制监听一次有效的拖动行为，这里的有效是开发者自己来定制的。在使用时，需要设置一个 pointId，表示我们希望追踪手势事件的手指标识符。
当该手指抬起时，如果有其他手指还在屏幕上，则会选择其中一根手指来继续追踪手势；而如果已经没有手指在屏幕上了，则返回null。如果在调用awaitTouchSlopOrCancellation前，
pointId 对应手指没有产生 ACTION_DOWN事 件，则也会返回 null。

onTouchSlopReached 会在超过 ViewConfiguration 中所设定的阈值 touchSlop 时回调。如果根据事件信息判断出我们希望接收这次手势事件，则应该通过 change 调用 consumePositionChange
进行消费，此时 awaitTouchSlopOrCancellation 会恢复执行，并返回当前 PointerInputChange。如果不消费，则会继续挂起检测滑动位移。
 */
    var horizontalVelocity by remember {
        mutableStateOf(0f)
    }

    var verticalVelocity by remember {
        mutableStateOf(0f)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Velocity",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("Horizontal: %.2f Vertical: %.2f", horizontalVelocity, verticalVelocity),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val offset = remember {
                Animatable(Offset.Zero, Offset.VectorConverter)
            }

            Box(
                Modifier
                    .size(350.dp)
                    .background(Color.Gray)
                    .pointerInput(Unit) {

                        offset.updateBounds(
                            lowerBound = Offset.Zero,
                            upperBound = Offset(320.dp.toPx(), 320.dp.toPx())
                        )

                        coroutineScope {

                            forEachGesture {

                                //对于拖动手势，首先需要使用 awaitFirstDown 获取 ACTION_DOWN 手势事件信息。值得注意的是，当上一轮 Fling 未结束本轮
                                // 手势便开始时，可以使用 Animatable 提供的 stop 方法来中断结束上一轮动画。
                                val down = awaitPointerEventScope { awaitFirstDown() }
                                offset.stop()

                                awaitPointerEventScope {

                                    //利用 awaitTouchSlopOrCancellation 检测当前是否为有效拖动手势，当检测成功后，便可以使用 drag 来监听具体的拖动手势事件。
                                    var validDrag: PointerInputChange?
                                    do {
                                        validDrag = awaitTouchSlopOrCancellation(down.id) { change, _ ->
                                            change.consumePositionChange()
                                        }
                                    } while (validDrag != null && !validDrag.positionChangeConsumed())

                                    //当手指离开屏幕时，需要根据离屏时的位置信息与速度信息来计算组件最终会停留的位置。位置信息可以利用 offset 获取到，
                                    // 而速度信息的获取则需要使用速度追踪器 VelocityTracker。
                                    //当发生拖动时，首先使用 snapTo 移动组件偏移位置。既然追踪手势速度，就需要将手势信息告知 VelocityTracker，通过
                                    // addPosition 实时告知 VelocityTracker 当前的手势位置，VelocityTracker 便可以实时计算出当前的手势速度了。
                                    if (validDrag != null) {
                                        val velocityTracker = VelocityTracker()
                                        var dragAnimJob: Job? = null
                                        drag(validDrag.id) {
                                            dragAnimJob = launch {
                                                offset.snapTo(offset.value + it.positionChange())
                                                velocityTracker.addPosition(
                                                    it.uptimeMillis,
                                                    it.position
                                                )
                                                horizontalVelocity = velocityTracker.calculateVelocity().x
                                                verticalVelocity = velocityTracker.calculateVelocity().y
                                            }
                                        }
                                        horizontalVelocity = velocityTracker.calculateVelocity().x
                                        verticalVelocity = velocityTracker.calculateVelocity().y
                                        // 当手指离开屏幕时，可以利用 VelocityTracker 与 Offset 获取到实时速度信息与位置信息。之后，可以利用 splineBasedDecay
                                        // 创建一个衰值推算器，这可以帮助我们根据当前速度与位置信息推算出组件 Fling 后停留的位置。由于最终位置可能会超出屏幕，
                                        // 所以还需设置数值上下界，并采用 animateTo 进行 Fling 动画。由于我们希望的是组件最终会缓缓地停下，所以这里采用的是
                                        // LinearOutSlowInEasing 插值器。
                                        val decay = splineBasedDecay<Offset>(this)
                                        val targetOffset = decay
                                            .calculateTargetValue(
                                                Offset.VectorConverter,
                                                offset.value,
                                                Offset(horizontalVelocity, verticalVelocity)
                                            )
                                            .run {
                                                Offset(x.coerceIn(0f, 320.dp.toPx()), y.coerceIn(0f, 320.dp.toPx()))
                                            }
                                        dragAnimJob?.cancel()
                                        launch {
                                            offset.animateTo(targetOffset, tween(2000, easing = LinearOutSlowInEasing))
                                        }
                                    }

                                }
                            }
                        }
                    }
            ) {

                Box(modifier = Modifier
                    .offset {
                        IntOffset(
                            x = offset.value.x.roundToInt(),
                            y = offset.value.y.roundToInt()
                        )
                    }
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                )
            }
        }
    }
}