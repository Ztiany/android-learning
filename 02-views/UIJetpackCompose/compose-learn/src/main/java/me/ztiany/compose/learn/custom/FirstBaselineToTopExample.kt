package me.ztiany.compose.learn.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import timber.log.Timber

/** 演示 layout 修饰符的使用*/
@Composable
fun FirstBaselineToTopExample() {
    Column {

        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Text("【LayoutModifier】", textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
            Spacer(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))
            Text("【Padding】", textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        }

        Divider()

        Row(modifier = Modifier.background(Color.Green)) {
            Text(
                "Hi there!",
                Modifier
                    .firstBaselineToTop(24.dp)
                    .weight(1F)
            )

            Text(
                "Hi there!",
                Modifier
                    .padding(top = 24.dp)
                    .weight(1F)
            )
        }
    }
}

/*
layout 修饰符是用来修饰 LayoutNode 的宽高与原有内容在新宽高下摆放位置的。当使用 layout 修饰符时，我们传入的回调包含了两个信息：

    - measurable: 表示被修饰 LayoutNode 的测量句柄，通过内部 measure 方法完成 LayoutNode 的测量。
    - constraints: 表示来自父 LayoutNode 的布局约束。

需要注意的是，每个 LayoutNode 只允许被测量一次。即 measure 只允许调用一次。
 */
fun Modifier.firstBaselineToTop(
    /*文字的 firstBaseline 到父组件顶部的高度*/
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->

    /*
    这里使用 measurable 的 measure() 方法来测量 LayoutNode，这里将 constraints 参数直接传入 measure 中，这说明我们是将父
     LayoutNode 提供的布局约束直接提供给被修饰的 LayoutNode 进行测量了。测量结果会包装在 Placeable 实例中返回。
     */
    val placeable = measurable.measure(constraints)

    /*
    现在 Text 组件本身的 LayoutNode 已经完成了测量，需要根据测量结果计算被修饰后的 LayoutNode 应占有的宽高并通过 layout 方法
    进行指定。

    我们期望的宽度就是文本宽度，而高度是指定的 Text 顶部到文本基线的高度与文本基线到 Text 底部的高度之和。
     */
    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    // 获取基线的高度
    val firstBaseline = placeable[FirstBaseline]
    // 应当摆放的顶部位置为所设置的顶部到基线的距离减去实际组件内容顶部到极限的高度
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    // 该组件占有的高度为应摆放高度加上实际内容的高度
    val height = placeable.height + placeableY

    Timber.tag("firstBaselineToTop").d("firstBaseline = %s", firstBaseline)
    Timber.tag("firstBaselineToTop").d("placeableY = %s", placeableY)
    Timber.tag("firstBaselineToTop").d("height = %s", height)

    //通过调用 layout(width, height) 方法指定可组合项的尺寸，该方法还会提供一个用于放置被封装元素的 lambda。【layout 影响的是父组件的 size】
    layout(placeable.width, height) {// 这里是指定组件的大小
        //  这里就是指定组件的放置位置【即在指定父组件的 size 后，在该 size 范围内摆放自己】
        // 通过调用 placeable.place(x, y) 将被封装的元素放到屏幕上。如果未放置被封装的元素，它们将不可见。
        // 这里的 placeRelative 方法会根据布局方向自动调整位置，比如阿拉伯国家一般更习惯于 RTL 这种布局方向。
        placeable.placeRelative(0, placeableY)
    }
}
