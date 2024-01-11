package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.runtime.Composable


/*
    其他的 AnimationSpec

        FloatAnimationSpec 只针对 Float 类型的动画，为什么要针对 Float 类型的动画呢？其实它们是 Compose 动画拿来做底层辅助运算的，作为开发者，我们不需要使用。

            FloatAnimationSpec (androidx.compose.animation.core)
                FloatTweenSpec (androidx.compose.animation.core)
                FloatSpringSpec (androidx.compose.animation.core)


        下面这些前面已经介绍过了。

            FiniteAnimationSpec (androidx.compose.animation.core)
                DurationBasedAnimationSpec (androidx.compose.animation.core)
                    KeyframesSpec (androidx.compose.animation.core)
                    SnapSpec (androidx.compose.animation.core)
                    TweenSpec (androidx.compose.animation.core)
                RepeatableSpec (androidx.compose.animation.core)
                SpringSpec (androidx.compose.animation.core)
            InfiniteRepeatableSpec (androidx.compose.animation.core)


            // ReversedSpec 和 CombinedSpec 是 VectorDrawable 动画的子类型，不是我们这里所说的动画。
            ReversedSpec (androidx.compose.animation.graphics.vector)
            CombinedSpec (androidx.compose.animation.graphics.vector)


        前面还提到了：
            VectorizedAnimationSpec：其继承体系与 FiniteAnimationSpec 相同，其实 Compose 动画底层并不是直接对各种元素进行计算，比如说有 DP、Color 等，
                                     所以需要对它们进行转换，把它们转换为一类叫做 AnimationVector 的类型，然后对 Vector 进行计算，比如有：

                                        AnimationVector1D (androidx.compose.animation.core) 表示对 1 个 float 值做转换。
                                        AnimationVector2D (androidx.compose.animation.core) 表示对 2 个 float 值做转换。比如用于坐标。
                                        AnimationVector3D (androidx.compose.animation.core) 表示对 3 个 float 值做转换。
                                        AnimationVector4D (androidx.compose.animation.core) 表示对 4 个 float 值做转换。比如用于颜色的 ARGP。

                                     VectorizedAnimationSpec 就是用于对这些 AnimationVector 进行计算的。这个叫做矢量化。


         至于 DecayAnimationSpec 以及 FloatDecayAnimationSpec，就结合后面的消散（官方叫衰减）动画一起讲解。
 */
@Composable
fun S409_AnimationSpec_OtherSpecs() {

}