package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun S412_Animation_Stop_Cancel() {
    Row(Modifier.fillMaxSize()) {
        Box1()
        Box2()
        Box3()
    }
}

@Composable
private fun Box1() {
    BoxWithConstraints {
        /*
            1. 如果多次调用 animateTo，那么 Compose 会先停止上一次的动画，然后再开始新的动画。它会通过 CancellationException 来停止动画。
            2. Animatable.stop() 可以停止动画，但是注意要在一个新的协程中调用，因为 animateTo 是一个挂起函数，如果在同一个协程中调用，那么它会阻塞当前协程，导致 stop 无法执行。这个 API 也会触发 CancellationException。
            3. 除了手动停止动画，奤可以通过 paddingAnimatable.updateBounds() 设置动画边界，当动画到达边界时，动画会自动停止。【Compose 把这个停止归于正常的结束，不会抛出 CancellationException】
            4. animateTo 的返回值是一个 AnimationResult，通过它可以判断动画是正常结束还是被取消了。
            5. 有时我们是对一个多维的数值做动画，比如对一个坐标做动画，Compose 对多维数值动画的触边处理是：只要一个维度到达边界，那么整个动画就会停止。如果不想要这种效果，那么就要自己做处理。两种方法：
                1. 根据 animateTo 的返回值，可以拿到动画停止前的信息，然后如果某个维度没有达到边界，那么就开启一个新的动画。
                2. 单独对每个维度做动画，这样就不会出现一个维度到达边界，导致整个动画停止的情况。
         */
        val paddingAnimatable = remember {
            Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
        }

        LaunchedEffect(Unit) {
            try {
                delay(400)
                paddingAnimatable.animateDecay(1000.dp, exponentialDecay())
            } catch (e: CancellationException) {
                Timber.w("1-Animation has been canceled")
            }
        }

        // updateBounds 可以配合 BoxWithConstraints 使用，因为 BoxWithConstraints 提供了最大的宽高。
        paddingAnimatable.updateBounds(upperBound = maxHeight - 100.dp/*下边界，记得减去自己的高度*/)

        LaunchedEffect(Unit) {
            try {
                delay(500)
                paddingAnimatable.animateDecay(4000.dp, exponentialDecay())
            } catch (e: CancellationException) {
                Timber.w("2-Animation has been canceled")
            }
        }

        Box(
            Modifier
                .padding(0.dp, paddingAnimatable.value, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Yellow)
        ) {
            Text(text = "触边停止", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun Box2() {
    BoxWithConstraints {
        /*
            6. 可以利用 Animatable.animateTo 的返回值，来判断动画是否正常结束。从而实现一个触边反弹的效果。
               注意：利用这种方式实现的反弹效果是不那么准确的，因为它是通过每一帧计算出来的位置来推算出的速度，而不是真实的实时速度。
         */
        val paddingAnimatable = remember {
            Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
        }

        val decay = remember { exponentialDecay<Dp>(frictionMultiplier = 0.5F) }

        // updateBounds 可以配合 BoxWithConstraints 使用，因为 BoxWithConstraints 提供了最大的宽高。
        paddingAnimatable.updateBounds(lowerBound = 0.dp, upperBound = maxHeight - 100.dp/*下边界，记得减去自己的高度*/)

        LaunchedEffect(Unit) {
            delay(500)
            var result = paddingAnimatable.animateDecay(8000.dp, decay)
            while (result.endReason == AnimationEndReason.BoundReached) {
                result = paddingAnimatable.animateDecay(
                    -result.endState.velocity,/* 负数表示反向 */
                    animationSpec = decay
                )
            }
        }

        Box(
            Modifier
                .padding(0.dp, paddingAnimatable.value, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Yellow)
        ) {
            Text(text = "反弹的 Box", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun Box3() {
    BoxWithConstraints {
        /*
            7. 利用一个数学技巧，可以模拟出精确的反弹动画。其实就是不让动画触发边界停止，而是通过计算来模拟出反弹的效果。
         */
        val paddingAnimatable = remember {
            Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
        }

        val decay = remember { exponentialDecay<Dp>(frictionMultiplier = 0.5F) }

        LaunchedEffect(Unit) {
            delay(500)
            paddingAnimatable.animateDecay(8000.dp, decay)
        }

        // 通过计算，模拟出精确的反弹效果。
        val paddingY = remember(paddingAnimatable.value) {
            var usedY = paddingAnimatable.value
            while (usedY >= (maxHeight - 100.dp) * 2) {
                usedY -= (maxHeight - 100.dp) * 2
            }
            if (usedY < maxHeight - 100.dp) {
                usedY
            } else {
                (maxHeight - 100.dp) * 2 - usedY
            }
        }

        Box(
            Modifier
                .padding(0.dp, paddingY, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Yellow)
        ) {
            Text(text = "精确反弹的 Box", modifier = Modifier.align(Alignment.Center))
        }
    }
}