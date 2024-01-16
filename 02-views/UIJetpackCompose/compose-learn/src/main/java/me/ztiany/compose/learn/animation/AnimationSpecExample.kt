package me.ztiany.compose.learn.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
演示 AnimationSpec 的使用：

什么是 AnimationSpec：AnimationSpec 是一个单方法接口，泛型 T 是当前动画数值类型，vectorize 用来创建一个 VectorizedAnimationSpec，即一个矢量动画的配置。
矢量动画是通过函数运算生成的，而 AnimationVector 就是用来参与计算的动画矢量。TwoWayConverter 将 T 类型的状态值转换成参与动画计算的矢量数据。

Compose 提供了多种 AnimationSpec 的子类，分别基于不同的 VectorizedAnimationSpec 实现不同动画效果的计算。例如：

1.  TweenSpec 用来实现两点间的补间动画
2.  SpringSpec 实现基于物理效果的动画
3.  SnapSpec 是一个即时生效的动画

spring 弹跳动画：可用来创建一个基于物理特性的弹跳动画，它的动画估值将在当前值和目标值之间按照弹簧物理运动轨迹进行变化。

    spring 有三个参数：dampingRatio、stiffness 和 visibilityThreshold，前两个参数主要用来控制弹跳动画的动画效果。

        dampingRation：表示弹簧的阻尼比。阻尼比用于描述弹簧振动逐渐衰减的状况。阻尼比可以定义振动从一次弹跳到下一次弹跳所衰减的速度有多快。
                                      以下是不同阻尼比下的弹力衰减情况：
                                            • 当 dampingRation>1 时会出现过阻尼现象，这会使对象快速地返回到静止位置。
                                            • 当 dampingRation=1 时会出现临界阻尼现象，这会使对象在最短时间内返回到静止位置。
                                            • 当 1>dampingRation>0 时会出现欠阻尼现象，这会使对象围绕最终静止位置进行多次反复震动。
                                            • 当 dampingRation=0 时会出现无阻尼现象，这会使对象永远振动下去。
        注意阻尼比不能小于零。同时，Compose 为 spring 提供了一组常用的阻尼比常量。如果不额外指定，默认会采用 DampingRatioNoBouncy。
         此时会出现临界阻尼现象，对象会在很短的时间内恢复静止而不发生振动。

        stiffness：定义弹簧的刚度值。刚度值越大，弹簧到静止状态的速度越快。Compose 为 stiffness 定义的常量如下。
                - StiffnessHigh
                - StiffnessMedium
                - StiffnessMediumLow
                - StiffnessLow
                - StiffnessVeryLow
        stiffness 的默认值为 StiffnessMedium， 表示到静止过程的速度适中。很多动画 API 内部对 AnimationSpec 使用的默认值均为 spring，例如 animate*AsState
        以及 updateTransition 等。因为 spring 的动画效果基于物理原理，使动画更加真实自然。注意：刚度值必须大于 0。

        visibilityThreshold：最后一个参数 visibilityThreshold 是一个泛型，此泛型与 targetValue 类型保持一致。由开发者指定一个阈值，当动画到达这个阈值时，动画会立即停止。



tween 补间动画：使用 tween 可以创建一个 TweenSpec 实例，TweenSpec 是 DurationBasedAnimationSpec 的子类。从基类名字可以感受到，
                                TweenSpec 的动画必须在规定时间内完成，所以它不能像 SpringSpec 那样完全基于物理规律进行动画，它的动画效果是基于
                                时间参数计算的，可以使用 Easing 来指定不同的时间曲线动画效果。

                                tween 补间动画的三个参数：

                                        1. 动画时长
                                        2. 动画延迟多久执行
                                        3. 衰减曲线



keyframes 关键帧动画：相对于 tween 动画只能在开始和结束两点之间应用动画效果，keyframes 可以更精细地控制动画，
                                            它允许在开始和结束之间插入关键帧节点，节点与节点之间的动画过渡可以应用不同效果。



repeatable 循环动画：使用 repeatable 可以创建一个 RepeatableSpec 实例。前面所介绍的动画都是单次动画，而这里的 repeatable 是一个可循环播放的动画，
                                        可以指定 TweenSpec 或者 KeyFramesSpec 以及循环播放的方式。repeatable 函数的参数 animation 必须是一个 DurationBasedAnimationSpec 子类，
                                        spring 不支持循环播放。这是可以理解的，因为一个永动的弹簧确实违背物理定律。



infiniteRepeatable 无限循环动画：infiniteRepeatable 顾名思义，就是无限执行的 RepeatableSpec，因此没有 iterations 参数。
                                                            它将创建并返回一个 InfiniteRepeatableSpec 实例。Transition 中的 rememberInfiniteTransition，这是一种无限循环的
                                                            Transition 动画，因此它只能对无限循环的动画进行组合，它的 animationSpec 必须使用 infiniteRepeatable 来创建。



snap 快闪动画：snap 会创建一个 SnapSpec 实例，这是一种特殊动画，它的 targetValue 发生变化时，当前值会立即更新为 targetValue。由于没有中间过渡，
                            动画会瞬间完成，常用于跳过过场动画的场景。我们也可以设置 delayMillis 参数来延迟动画的启动时间。



使用 Easing 控制动画节奏：Tween 与 Keyframes 都是基于时间计算的动画，而 Easing 本质上就是一个基于时间参数的函数，输入值表示当前动画在时间上的进度，
                                                返回值是则是当前 value 的进度，1.0 表示已经达到 targetValue。不同的 Easing 算法可以实现不同的动画加速、减速效果，因此也可以
                                                将 Easing 理解为动画的瞬时速度。Compose 内部提供了多种内置的 Easing 曲线，可满足大多数的使用场景，具体可以参考官方文档。
 */
@Composable
fun AnimationSpecExample() {
    val change = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { change.value = !change.value }, Modifier.fillMaxWidth()) {
            Text(text = "Change")
        }
        AnimationSpecItems(change, tween(durationMillis = 1000))

        AnimationSpecItems(change, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = 10F))
        AnimationSpecItems(change, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = 10F))
        AnimationSpecItems(change, spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = 10F))

        AnimationSpecItems(change, keyframes {
            durationMillis = 1000
            0.dp at 0 with LinearOutSlowInEasing//for 0-800 ms
            50.dp at 800 with FastOutSlowInEasing
        })

        AnimationSpecItems(
            change,
            repeatable(
                iterations = 3,
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        AnimationSpecItems(
            change,
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        AnimationSpecItems(change, snap())

        AnimationSpecItems(change, tween(durationMillis = 1000, easing = FastOutSlowInEasing))
        AnimationSpecItems(change, tween(durationMillis = 1000, easing = LinearOutSlowInEasing))
        AnimationSpecItems(change, tween(durationMillis = 1000, easing = FastOutLinearInEasing))
        AnimationSpecItems(change, tween(durationMillis = 1000, easing = LinearEasing))

        //另外还可以使用 CubicBezierEasing 三阶贝塞尔曲线自定义任意 Easing
        AnimationSpecItems(change, tween(durationMillis = 1000, easing = CubicBezierEasing(.4F, 0F, .2F, 1F)))
        AnimationSpecItems(change, tween(durationMillis = 1000, easing = CubicBezierEasing(0F, 0F, .2F, 1F)))
    }
}

@Composable
private fun AnimationSpecItems(state: MutableState<Boolean>, animationSpec: AnimationSpec<Dp>) {
    val offsetX by animateDpAsState(
        targetValue = if (!state.value) 0.dp else 200.dp,
        animationSpec = animationSpec
    )

    Box(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "", modifier = Modifier.offset(x = offsetX))
    }
}

@Composable
private fun AnimationSpecItems(animationSpec: AnimationSpec<Dp>) {
    val offsetX by animateDpAsState(
        targetValue = 200.dp,
        animationSpec = animationSpec
    )

    Box(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "", modifier = Modifier.offset(x = offsetX))
    }
}