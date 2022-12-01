package me.ztiany.compose.foundation.custom

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/** 演示 LayoutComposable 的使用*/
@Composable
fun SimpleColumnExample() {
    CustomBasicColumn {
        Text("MyBasicColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

/*
LayoutModifier 可以类比于定制单元View。如果想在 Compose 中类似定制"ViewGroup"，就需要使用 LayoutComposable 了。

LayoutComposable 需要填写三个参数：modifier、content、measurePolicy。

    - Modifier 表示是由外部传入的修饰符
    - content 就是我们声明的子组件信息
    - measurePolicy 表示测量策略，默认场景下只实现 measure 即可，如果还想实现固有特性测量，还需要重写 Intrinsic 系列方法。

一个重要提示：如果我们在 LayoutModifier 的 measure 方法或 LayoutComposable 中读取了某个可变状态，当该状态更新时，
会导致当前组件重新进行布局阶段，故也被称作重排。如果组件的大小或位置发生了更新，则还会重新进行接下来的绘制阶段。

下面通过 LayoutComposable 自己实现一个 Column。
 */
@Composable
fun CustomBasicColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        /*
        和 LayoutModifier 一样，需要对所有子 LayoutNode 进行一次测量。但与 LayoutModifier 不同的是，这里的 measurables 是一个 List，
        而 LayoutModifier 中只是一个 measurable 对象。

        这里直接传入 constraints 表示不对子组件进行限制。
         */
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        /*
        接下来仍然需要计算当前 LayoutNode（CustomBasicColumn） 的宽高。这里的实现比较简单，将宽高直接设置为当前布局约束中的最大宽高，并仍然通过
        layout() 方法指定。
         */
        layout(constraints.maxWidth, constraints.maxHeight) {
            // 确定完整个布局的宽高后，就在里面摆放所有的子组件：

            // 我们是需要一个 Column，所以是一行一行的往下摆放，因此需要记录累加的高度：
            var yPosition = 0
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)
                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}
