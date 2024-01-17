package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import timber.log.Timber


@Composable
fun S504_LayoutModifier() {
    /*
        Modifier.layout 仅用于单个元素，它的作用是修改所修饰元素的 size 和 position。

        在 layout 里面，也拿不到父元素的布局信息，甚至连自己的属性都拿不到。所以它的功能不是很强大。

        Modifier.layout 的应用场景是给组件在大小和位置方面增加装饰作用，它不能干涉组件的内容。即只能从外部去修改组件的大小和位置，不能修改组件的内部子组件的测量和布局规则。
     */
    Column {
        Box(modifier = Modifier.background(Color.Red)) {
            Text(text = "Hello World, ！！ This A Text.", Modifier.layout { measurable, constraints ->
                // measurable 其实就是对应 Text 的一个测量对象，调用 measure 方法，就可以确定 Text 大小。
                Timber.w("constraints: $constraints")
                val placeable = measurable.measure(constraints)

                val size = maxOf(placeable.width, placeable.height)// 改为正方形
                // layout 函数其实就是用来创建 MeasureResult。
                layout(size, size/* 设置布局需要i的宽高 */) {
                    // 设置摆放位置
                    placeable.placeRelative(0, 0)
                }
            })
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Box(modifier = Modifier.background(Color.Green)) {
            Text(text = "Hello World, ！！ This A Text.", Modifier.layout { measurable, constraints ->
                // 实现一个 padding 的效果
                val padding = 10.dp.roundToPx()
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth - padding * 2,
                        maxHeight = constraints.maxHeight - padding * 2,
                    )
                )

                // layout 函数其实就是用来创建 MeasureResult。
                layout(placeable.width + padding * 2, placeable.height + padding * 2) {
                    // 设置摆放位置
                    placeable.placeRelative(padding, padding)
                }
            })
        }
    }

}