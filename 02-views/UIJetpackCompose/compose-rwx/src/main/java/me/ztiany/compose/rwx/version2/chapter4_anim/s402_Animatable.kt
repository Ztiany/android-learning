package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
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
 API: Animatable，定义在 androidx/compose/animation/SingleValueAnimation.kt
 用途：流程定制型动画
 */
@Composable
fun S402_Animatable() {
    /*
       说明：
            Animatable 默认只对 float 类型做动画，如果要对其他类型做动画，需要提供 TwoWayConverter。
            对于常见的类型，比如 dp，Compose 提供了开箱即用的 Converter。

        有四种维度的 Converter：
            AnimationVector1D (androidx.compose.animation.core) 表示对 1 个 float 值做转换。
            AnimationVector2D (androidx.compose.animation.core) 表示对 2 个 float 值做转换。
            AnimationVector3D (androidx.compose.animation.core) 表示对 3 个 float 值做转换。
            AnimationVector4D (androidx.compose.animation.core) 表示对 4 个 float 值做转换。比如用于颜色的 ARGP。

       Animatable 可以对动画过程进行精细编排，但是没有那么方便使用。
    */

    var big by remember {
        mutableStateOf(false)
    }

    val size = remember(big) {
        if (big) {
            106.dp
        } else {
            58.dp
        }
    }

    val sizeAnimatable = remember {
        Animatable(initialValue = size, typeConverter = Dp.VectorConverter)
    }

    LaunchedEffect(big) {
        // 设置初始值（瞬间到达）
        sizeAnimatable.snapTo(if (big) 292.dp else 0.dp)
        // 然后开启动画
        sizeAnimatable.animateTo(size)
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}