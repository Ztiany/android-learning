package me.ztiany.compose.learn.gesture.pointerinput.awaiteach

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/** 演示 awaitDragOrCancellation 的使用 */
@SuppressLint("DefaultLocale")
@Composable
fun AwaitTouchSlopOrCancellationViews() {
    /*
    awaitTouchSlopOrCancellation 用于定制监听一次有效的拖动行为，这里的有效是开发者自己来定制的。在使用时，需要设置一个 pointId，
    表示我们希望追踪手势事件的手指标识符。

    当该手指抬起时，如果有其他手指还在屏幕上，则会选择其中一根手指来继续追踪手势；而如果已经没有手指在屏幕上了，则返回 null。如果
    在调用 awaitTouchSlopOrCancellation 前， pointId 对应手指没有产生 ACTION_DOWN 事件，则也会返回 null。

    onTouchSlopReached 会在超过 ViewConfiguration 中所设定的阈值 touchSlop 时回调。如果根据事件信息判断出我们希望接收这次手势事件，
    则应该通过 change 调用 consumePositionChange 进行消费，此时 awaitTouchSlopOrCancellation 会恢复执行，并返回当前 PointerInputChange。
    如果不消费，则会继续挂起检测滑动位移。
     */
    var horizontalVelocity by remember {
        mutableFloatStateOf(0f)
    }

    var verticalVelocity by remember {
        mutableFloatStateOf(0f)
    }

    val scope = rememberCoroutineScope()

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

                            awaitEachGesture {

                                /*
                                对于拖动手势，首先需要使用 awaitFirstDown 获取 ACTION_DOWN 手势事件信息。值得注意的是，
                                当上一轮 Fling 未结束，而本轮手势便开始时，可以使用 Animatable 提供的 stop 方法来中断结束上一轮动画。
                                 */
                                val down = awaitFirstDown()
                                scope.launch {
                                    offset.stop()
                                }

                                // 利用 awaitTouchSlopOrCancellation 检测当前是否为有效拖动手势，当检测成功后，便可以使用 drag 来监听
                                // 具体的拖动手势事件。
                                var validDrag: PointerInputChange?
                                do {
                                    validDrag = awaitTouchSlopOrCancellation(down.id) { change, _ ->
                                        if (change.positionChange() != Offset.Zero) change.consume()
                                    }
                                } while (validDrag != null && !validDrag.isConsumed)

                                /*
                                当手指离开屏幕时，需要根据离屏时的位置信息与速度信息来计算组件最终会停留的位置。位置信息可以
                                利用 offset 获取到，而速度信息的获取则需要使用速度追踪器 VelocityTracker。

                                当发生拖动时，首先使用 snapTo 移动组件偏移位置。既然追踪手势速度，就需要将手势信息告知 VelocityTracker，
                                通过 addPosition 实时告知 VelocityTracker 当前的手势位置，VelocityTracker 便可以实时计算出当前的手势速度了。
                                 */
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
                                    /*
                                    当手指离开屏幕时，可以利用 VelocityTracker 与 Offset 获取到实时速度信息与位置信息。之后，可以利用
                                     splineBasedDecay 创建一个衰值推算器，这可以帮助我们根据当前速度与位置信息推算出组件 Fling 后停留的位置。
                                     由于最终位置可能会超出屏幕，所以还需设置数值上下界，并采用 animateTo 进行 Fling 动画。由于我们希望的是
                                     组件最终会缓缓地停下，所以这里采用的是 LinearOutSlowInEasing 插值器。
                                     */
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