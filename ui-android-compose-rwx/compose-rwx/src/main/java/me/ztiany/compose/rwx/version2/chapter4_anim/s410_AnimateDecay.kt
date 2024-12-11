package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/*
    消散（衰减）动画：有个初始速度的动画，有一个阻力，动画会逐渐减速，直到停止。
    用于惯性滑动等场景，比如松手之后，列表会继续滚动一段距离。
 */
@Composable
fun S410_AnimateDecay() {
    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    val paddingAnimatable = remember {
        Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
    }

    Box(
        Modifier
            .padding(0.dp,paddingAnimatable.value,0.dp,0.dp)
            .size(100.dp)
            .background(Color.Yellow)
            .clickable {
                scope.launch {
                    /*
                       animateTo 的核心是有一个目标值，然后根据这个目标值，和动画的配置，动画会逐渐趋近于目标值。

                       animateDecay 的核心是有一个初始速度，然后根据这个初始速度，和动画的配置，动画会逐渐减速，直到停止。它没有精确的目标值，只有初始速度。
                       与 animateTo 相比，它的动画过程是不可控的，但是它的动画过程更加真实，更加符合物理规律。

                       animateDecay 的参数：
                            initialVelocity: T：初始速度，这里的 T 是动画值的类型。
                            animationSpec: DecayAnimationSpec<T>：动画的配置，这里的 T 是动画值的类型。显然只能是 DecayAnimationSpec 的子类型。
                            block: (Animatable<T, V>.() -> Unit)? = null：监听每一帧动画的回调。


                       DecayAnimationSpec 可以通过下面的方法创建：

                            splineBasedDecay<>()：Spline 是一种数学曲线，它可以用于模拟物理规律。是安卓自带的惯性滚动的算法。splineBasedDecay 的算法就是照搬的 OverScroller。
                                                  虽然 splineBasedDecay 是带泛型的，但是它应该只用于像素的计算，也就说它的泛型应该是 Int，而不应该是 Dp。因为它会根据像素密度自动做修正。

                            rememberSplineBasedDecay()：它是一个带 remember 的 splineBasedDecay<>()，而且自动传入了 density 参数。density 越大，惯性阻力越大。

                            exponentialDecay<>()：指数衰减算法，没有修正，不会根据像素密度自动做修正。它的泛型可以是除了像素之外的任何类型，比如 Dp。
                                exponentialDecay<>() 的参数：
                                    frictionMultiplier：摩檫力系数，越大，惯性阻力越大。
                                    absVelocityThreshold：速度阈值绝对值，当速度小于这个值时，动画会停止。必须大于 0。

                            exponentialDecay 没有 remember 版本，因为它没有 density 参数，所以不需要 remember。splineBasedDecay 有 density 参数，它在 density 改变时，会自动重新计算。
                            虽然官方没有提供 remember 版本的 exponentialDecay，但是在 Composable 内部，我们也应该用 remember 包裹它，这样可以避免重复创建。

                   */
                    paddingAnimatable.animateDecay(1000.dp, exponentialDecay())
                }
            })
}