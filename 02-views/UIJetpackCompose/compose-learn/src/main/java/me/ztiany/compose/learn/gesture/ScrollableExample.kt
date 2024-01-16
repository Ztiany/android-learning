package me.ztiany.compose.learn.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import timber.log.Timber

/** 演示 Scrollable 的使用*/
@Composable
fun ScrollableViews() {
    /*
    当视图组件的宽度或长度超出屏幕边界时，我们希望能滑动查看更多的内容。对于长列表场景，可以使用 LazyColumn 与 LazyRow 组件来实现。
    而对于一般组件，可以使用 Scrollable 系列修饰符来修饰组件，使其具备可滚动能力。

    Scrollable 系列修饰符包含了 horizontalScroll、verticalScroll 与 scrollable。

        horizontalScroll 水平滚动：当组件宽度超出屏幕边界时，可以使用horizontalScroll修饰符为组件增加水平滑动查看更多内容的能力。
            horizontalScroll 修饰符仅有一个必选参数 scrollState。可以使用 rememberScrollState 快速创建一个 scrollState 实例并传入即可。



        verticalScroll 垂直滚动：
                与 horizontalScroll 修饰符功能一样，当组件高度超出屏幕时，可以使用 verticalScroll 修饰符使组件在垂直方向上滚动。
                参数列表与 horizontalScroll 完全一致，使用方法也完全相同



         scrollable 修饰符：
                horizontalScroll 与 verticalScroll 都是基于 scrollable 修饰符实现的，scrollable 修饰符只提供了最基本的滚动手势监听，而上层 horizontalScroll 与
                verticalScroll 分别额外提供了滚动在布局内容方面的偏移。

                scrollable 修饰符的参数列表与 horizontalScroll、verticalScroll 也是非常相似的，我们需要输入一个 ScrollableState 滚动状态和一个 Orientation 方向。
                Orientation 仅有 Horizontal 与 Vertical 可供选择，这说明我们只能监听水平或垂直方向的滚动。

                ScrollState 中的 value 字段表示当前滚动位置，从源码中可以看到其实际上是一个可变状态。可以利用这个状态来处理手势逻辑，
                并且还可以使用 ScrollState l来动态控制组件发生滚动行为。

                滚动位置范围为 0～MAX_VALUE。默认场景下当手指在组件上向右滑动时，滚动位置会增大，向左滑动时，滚动位置会减小，直至滚动位置减少到 0。由于滚动位置默认初始值为 0，
                所以我们只能向右滑增大滚动位置。如果将 scrollable 中的 reverseDirection 参数设置为 true 时，那么此时手指向左滑滚动位置会增大，向右滑滚动位置会减小，这允许我们在初始
                位置向左滑动。scrollable 中的 reverseDirection 参数与 horizontalScroll 中的 reverseScrolling 参数是有区别的，实际上 reverseDirection 参数数值与 reverseScrolling 参数含义截然相反。
     */
    val scrollableState = rememberScrollState(0/*初始化时，可以通过 initial 参数来指定组件初始滚动位置的*/)

    /*    下面基于 scrollable 修饰符的滚动监听能力自己实现 horizontalScroll 修饰符。     */
    Row(
        Modifier
            // 如果利用 offset 修饰符使组件内容内容偏移。我们会发现当左滑时，原本位于屏幕外的内容进入屏幕时是一片空白，
            // 这是因为 Row 组件的默认测量策略导致超出屏幕的子组件宽度测量结果为零，此时就需要使用 layout 修饰符自己来定制组件布局了。
            //.offset(x = with(LocalDensity.current) { -scrollableState.value.toDp() })
            .height(136.dp)
            .scrollable(scrollableState, Orientation.Horizontal, reverseDirection = true)
            //我们需要创建一个新的约束，用于测量组件的真实宽度，主动设置组件所应占有的宽高尺寸空间，并根据组件的滚动偏移量来摆放组件内容。
            .layout { measurable, constraints ->
                //约束中默认最大宽度为父组件所允许的最大宽度，此处为屏慕宽度，这里将最大宽度设置为无限大。
                val placeable = measurable.measure(constraints.copy(maxWidth = Constraints.Infinity))
                Timber.d("constraints w*h: %dx%d", constraints.maxWidth, constraints.maxHeight)
                Timber.d("placeable w*h:  %dx%d", placeable.width, placeable.height)

                // 计算当前组件宽度与父组件所允许的最大宽度中取一个最小值
                // 如果组件超出屏幕，时 width 为屏幕宽度。如果没有超出，则为组件本文宽度
                val width = placeable.width.coerceAtMost(constraints.maxWidth)
                val height = placeable.height.coerceAtMost(constraints.maxHeight)
                //可滑动距离
                val scrollableDistance = placeable.width - width
                Timber.d("layout w*h: %dx%d, scrollableDistance: %d", width, height, scrollableDistance)

                layout(width, height) {
                    // 根据可滚动的距离来计算滚动位置
                    val scroll = scrollableState.value.coerceIn(0, scrollableDistance)
                    val offsetX = -scroll
                    Timber.d("scroll: scrollableState.value = ${scrollableState.value} offsetX=$offsetX")
                    placeable.placeRelativeWithLayer(offsetX, 0)
                }
            }
    ) {

        for (i in 0..20) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Blue)
            ) {
                Text(
                    text = i.toString(), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center, color = Color.White
                )
            }
        }
    }

}