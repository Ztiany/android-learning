package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
    AnimationSpec 用于指定动画细节，正如官方文档所述：

        AnimationSpec stores the specification of an animation, including：
            1) the data type to be animated
            2) the animation configuration

    AnimationSpec 有很多子类型

        FloatAnimationSpec (androidx.compose.animation.core)
            FloatTweenSpec (androidx.compose.animation.core)
            FloatSpringSpec (androidx.compose.animation.core)

        FiniteAnimationSpec (androidx.compose.animation.core)
            DurationBasedAnimationSpec (androidx.compose.animation.core)
                KeyframesSpec (androidx.compose.animation.core)
                SnapSpec (androidx.compose.animation.core)
                TweenSpec (androidx.compose.animation.core)
            RepeatableSpec (androidx.compose.animation.core)
            SpringSpec (androidx.compose.animation.core)
            ReversedSpec (androidx.compose.animation.graphics.vector)
            CombinedSpec (androidx.compose.animation.graphics.vector)

        InfiniteRepeatableSpec (androidx.compose.animation.core)

   除此之外，还有 VectorizedAnimationSpec、DecayAnimationSpec 以及 FloatDecayAnimationSpec。

   ------------------------------------------------------------------------------------
   TweenSpec 是 DurationBasedAnimationSpec 的一种，表示在指定的时间内，从起始值变化到结束值。
   默认的时长是 DefaultDurationMillis（300ms），曲线模型是 FastOutSlowInEasing。

   TweenSpec 的名词虽然与 View 系统的补间动画名字相同，虽然它们同源、但是从技术实现层面来看，它们没有联系。

   TweenSpec 的工作模式：指定动画时长和动画曲线模型（类似 Interpolator）就可以自动计算每一个时刻的动画值。

        val durationMillis: Int = DefaultDurationMillis：用于指定动画时长
        val delay: Int = 0：用户延时
        val easing: Easing = FastOutSlowInEasing：Easing 可以立即为“缓动”，就是上面所说的动画曲线模型。

    Easing 有四个预设值：
        FastOutSlowInEasing：先加速启动后减速停止。
        LinearOutSlowInEasing：入场速时度快，然后慢慢减速。
        FastOutLinearInEasing：加速离开。
        LinearEasing：匀速变化。

    除了 LinearEasing，其他三个都是用三阶贝塞尔曲线来实现的模型。在这个四个点组成的三阶贝塞尔曲线中，起点和终点是固定的，
    通过控制中间的两个点来控制曲线弧度，这样的一根曲线就用来为动画的变化建模。具体可以参考：https://cubic-bezier.com

    Tween 这个词的来源：来源于动画片，是 InBetween 的缩写，InBetween 的意思是为主画手写出的关键帧之间进行补帧。
    就类似于，安卓 View 系统里面，我们开发者只提供动画的开始值和结束值，剩余中间值就由系统来进行计算和补帧。
 */
@Composable
fun S403_AnimationSpec_TweenSpec() {

    var big by remember {
        mutableStateOf(false)
    }

    val size = remember(big) {
        if (big) {
            126.dp
        } else {
            58.dp
        }
    }

    val sizeAnimatable = remember {
        Animatable(
            initialValue = size, typeConverter = Dp.VectorConverter
        )
    }

    LaunchedEffect(big) {
        // 设置初始值（瞬间到达）
        sizeAnimatable.snapTo(if (big) 292.dp else 0.dp)
        // 然后开启动画
        sizeAnimatable.animateTo(size, animationSpec = TweenSpec(easing = LinearEasing))
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}