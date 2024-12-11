package me.ztiany.compose.learn.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/** 演示 SubcomposeLayout 的使用*/
@Composable
fun TwoTextsExampleBySubcomposeLayout(
    modifier: Modifier = Modifier,
    text1: String = "Left",
    text2: String = "Right"
) {
    SubcomposeRow(modifier.fillMaxWidth(),
        text = {
            Text(text = text1, Modifier.wrapContentWidth(Alignment.Start))
            Text(text = text2, Modifier.wrapContentWidth(Alignment.End))
        },
        divider = { height ->
            val heightDp = with(LocalDensity.current) { height.toDp() }
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .width(4.dp)
                    .height(heightDp)
            )
        }
    )
}

/*
使用 SubcomposeLayout 实现一个 Row。【实现 TwoTextsWithDivider.png 中展示的效果】

SubcomposeLayout 说明：
   （1）利用 SubcomposeLayout，可以做到将某个子组件的组合阶段延迟至其所依赖的同级子组件测量结束后进行，
            从而可以定制子组件间的组合、布局阶段顺序，以取代固有特性测量。
   （2）使用 SubcomposeLayout 可以允许组件根据需求来定制测量的顺序，与固有特性测量具有本质的区别。

对于这个需求，可以先测量两侧文本的高度，而后为 Divider 指定高度，再进行测量。与固有特性测量不同的是，在整个
过程中父组件是没有参与的。

接下来看看 SubcomposeLayout 组件是如何使用的：

    （1）其实 SubcomposeLayout 和 Layout 组件是差不多的。不同的是，此时需要传入一个 SubcomposeMeasureScope 类型 Lambda，
             打开接口声明可以看到其中仅有一个（名为 subcompose）。
    （2）subcompose 会根据传入的 slotId 和 Composable 生成一个 LayoutNode 用于构建子 Composition，最终会返回所有子 LayoutNode
            的 Measurable 测量句柄。其中 Composable 是我们声明的子组件信息。slotId 是用来让 SubcomposeLayout 追踪管理我们所创建的
            子 Composition 的，作为唯一索引每个 Composition 都需要具有唯一的 slotId.。

接下来看看如何使用 SubcomposeLayout 实现上述 TwoTextWithDivider 中的示例：

    （1）实际上可以把所有待测量的组件分为文字组件和分隔符组件两部分。
 */
@Composable
private fun SubcomposeRow(
    modifier: Modifier,
    text: @Composable () -> Unit,
    //（2）由于分隔符组件的高度是依赖文字组件的，所以声明分隔符组件时传入一个 Int 值作为测量高度。
    divider: @Composable (maxHeight: Int) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        //（3）使用 subcompose 来测量 text 中的所有 LayoutNode，并根据测量结果计算出最大高度。
        var maxHeight = 0
        val placeables = subcompose("text", text).map {
            val placeable = it.measure(constraints)
            maxHeight = placeable.height.coerceAtLeast(maxHeight)
            placeable
        }

        //（4）既然计算得到了文本的最大高度，接下来就可以将高度只传入分隔符组件中，完成组合阶段并进行测量。
        val dividerPlaceable = subcompose("divider") {
            divider(maxHeight)
        }.map {
            // （5）与前面固有特性测量中的一样，在测量 Divider 组件时，仍需重新复制一份 constraints 并将其 minWidth
            //          设置为 0，如果不修改，Divider组件宽度默认会与整个组件宽度相同。
            it.measure(constraints.copy(minWidth = 0))
        }
        assert(dividerPlaceable.size == 1) {
            "DividerScope Error!"
        }

        // （6）接下来分别对文字组件和分隔符组件进行布局。
        val midPosition = constraints.maxWidth / 2
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
            dividerPlaceable.forEach {
                it.placeRelative(midPosition, 0)
            }
        }
    }
}