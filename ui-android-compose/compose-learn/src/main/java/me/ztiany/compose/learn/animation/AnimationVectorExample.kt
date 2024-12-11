package me.ztiany.compose.learn.animation

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/*
演示 AnimationVector 的使用。

    矢量动画是基于动画矢量值 AnimationVector 计算的。 animate*AsState 基于 Animatable将Color、Float、Dp 等数据类型的数值转换成可动画类型，其本质就是将这些数据
    类型转换成 AnimationVector 参与动画计算。

TwoWayConverter：可以将任意 T 类型的数值转换为标准的 AnimationVector，反之亦然。这样，任何数值类型都可以随着动画改变数值。
                                    不同类型的数值可以根据需求与不同的 AnimationVectorXD 进行转换，这里的 X 代表了信息的维度。例如一个 Int 可以与 AnimationVector1D 相互转换，
                                    AnimationVector1D 只包含一个浮点数信息。

                                    同样，Size 中包含 width 和 height 两个维度的信息，可以与 AnimationVector2D 进行转换，Color 中包含 red、green、blue 和 alpha 4个数值，可以与
                                    AnimationVector4D 进行转换。当然 Compose 已经为常用类型提供了 TwoWayConverter 的拓展实现，可以在这些类型的伴生对象中找到它们，并且可以
                                    在 animate*AsState 中直接使用。

对于没有提供默认支持的数据类型，可以为其自定义对应的 TwoWayConverter。例如针对 MySize 这个自定义类型来自定义实现 TwoWayConverter，然后使用
animateValueAsState 为 MySize 添加动画效果。
*/
@Composable
fun AnimationVectorExample() {
    var change by remember { mutableStateOf(false) }

    val animatedSize: SizeA by animateValueAsState(targetValue = if (change) SizeA(30.dp,30.dp) else SizeA(300.dp, 300.dp),
        typeConverter = TwoWayConverter(
            convertToVector = {
                AnimationVector2D(it.width.value, it.height.value)
            },
            convertFromVector = {
                SizeA(it.v1.dp, it.v2.dp)
            }
        )) {

    }

    Box(
        Modifier
            .width(animatedSize.width)
            .height(animatedSize.height)
            .background(Color.Red)
            .clickable {
                change = !change
            }
    ) {

    }
}


private data class SizeA(
    val width: Dp,
    val height: Dp,
)