package me.ztiany.compose.learn.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

/** 演示如何实现 IntrinsicSize */
@Composable
fun TwoTextsExampleByCustom(
    modifier: Modifier = Modifier,
    text1: String = "Text1",
    text2: String = "Text2"
) {
    IntrinsicRow(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start)
                .layoutId("main"),
            text = text1
        )
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .layoutId("divider"),
        )
        Text(
            modifier = Modifier
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End)
                .layoutId("main"),
            text = text2
        )
    }
}

/*
使用 Layout 实现一个 Row。【实现 TwoTextsWithDivider.png 中展示的效果】
 */
@Composable
private fun IntrinsicRow(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier,
        measurePolicy = object : MeasurePolicy {

            /*
                因为我们的需求场景只使用了 Modifier.height(IntrinsicSize. Min)，所以仅重写 minIntrinsicHeight 方法就可以了。

                在重写的 minIntrinsicHeight 方法中，可以拿到子组件预先测量句柄 measurables。这个与  measure 方法中传入的 measurables 用法完全相同。在预先测量所有
                子组件后，就可以根据子组件的高度计算其中的高度最大值，此值将会影响到正式测量时父组件获取到的 constraints 的高度信息。此时 constraints 中的 maxHeight
                与 minHeight 都将被设置为返回的高度值，constraints 中的高度为一个确定值。
           */
            override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
                var maxHeight = 0
                measurables.forEach {
                    //coerceAtLeast 表示至少
                    maxHeight = it.minIntrinsicHeight(width).coerceAtLeast(maxHeight)
                }
                return maxHeight
            }

            /*
                接下来只需在定制的 Row 组件中使用固有特性测量就可以了：

                        此时，由于为 IntrinsicRow 声明了 Modifier.fillMaxWidth()，导致自定义 Layout 宽度是确定的（constraints 参数中 minWidth 与 maxWidth 相等），
                        又因为我们使用了固有特性测量，使组件高度也为一个确定值（constraints 参数中 minHeight 与 maxHeight 相等）。如果直接使用该 constraints 去测量 Divider，
                        会导致 Divider 的宽度也被设置为父组件宽度了，而实际上我们希望其宽度是组件自己决定的，宽度应为指定的 4dp，所以还应该对 constraints 进行复制并修改，
                        将 constraints 中的宽度最小值设置为 0，此时宽度将不会作为一个确定值影响Divider的测量过程。因为 constraints 中高度是确定的，这会使 Divider 组件的高度
                        被强制指定为该确定值。
             */
            override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
                val dividerConstraints = constraints.copy(minWidth = 0)
                val mainPlaceables = measurables.filter { it.layoutId == "main" }
                    .map {
                        it.measure(constraints)
                    }
                val dividerPlaceable = measurables.first { it.layoutId == "divider" }.measure(dividerConstraints)
                val midPosition = constraints.maxWidth / 2
                return layout(constraints.maxWidth, constraints.maxHeight) {
                    mainPlaceables.forEach {
                        it.placeRelative(0, 0)
                    }
                    dividerPlaceable.placeRelative(midPosition, 0)
                }
            }

        })
}