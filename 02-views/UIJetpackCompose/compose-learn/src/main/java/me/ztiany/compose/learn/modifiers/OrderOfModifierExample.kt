package me.ztiany.compose.learn.modifiers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun OrderOfModifierExample() {
    var value by remember {
        mutableStateOf(200.dp)
    }

    /*
        结论：
            1. measure 的发起点是顶部的 LayoutModifier，终点是 measurePolicy，然后是从顶部一直传递到底部，再从底部依次执行 layout 向上返回测量结果。
               因此，measure 的影响是顶从底部到顶部的，某个 Draw Modifier 受到其下游最近的 measure 的影响。
            2. place 的顺序是也是一样。但是产生的影响是顶部到底下。最先产生影响的是顶部，每次 place 的效果都会叠加。
    */
    Layout({

    }, measurePolicy = { _, constraints ->

        //测量链条的末端，返回给 2
        Timber.d("measurePolicy constraints: $constraints")
        Timber.d("measurePolicy layout: ${constraints.minWidth}x${constraints.minHeight}")
        layout(constraints.minWidth, constraints.minHeight) {
            Timber.d("measurePolicy layout")
        }

    }, modifier = Modifier
        .backgroundCopy("0", Color.Red)//画布还没有偏移
        .clickable {

        }

        .layout { measurable, constraints ->
            Timber.d("layout 1 constraints: $constraints")
            val placement = measurable.measure(constraints)//拿到的是下游 size 的测量结果
            Timber.d("layout 1 result: ${placement.width}x${placement.height}")
            layout(placement.width, placement.height) {//最终确定大小，最终以这个大小为准。
                Timber.d("layout 1 placeRelative-before")
                // 最靠近控件的 LayoutModifier 的 place 最先起作用。
                // 这里会影响后续绘制位置，相当于调整了画布的坐标。也就是说下面的 backgroundCopy("1", Color.Blue) 以这个位置为准。
                // 然后这里的 place 会影响 2 的 place，即 2 的 place 会基于 1 的 place 的结果。2 之后的绘制受到 2 的 place 的影响。
                placement.placeRelative(placement.width / 2, placement.height / 2)
                Timber.d("layout 1 placeRelative-after")
            }
        }
        .backgroundCopy("1", Color.Blue)

        .size(value)//查看 SizeModifier 的源码，可知 size 具有强制性，第一次设置后，后面都不能再改了，除非你自定义测量过程。
        .layout { measurable, constraints ->
            Timber.d("layout 2 constraints: $constraints")
            val placement = measurable.measure(constraints)//拿到的是 measurePolicy 测量测量的结果
            Timber.d("layout 2 result: ${placement.width}x${placement.height}")
            layout(placement.width, placement.height) {//返回给 1
                Timber.d("layout 2 placeRelative-before")
                placement.placeRelative(placement.width / 4, placement.height / 4)//受到前面 place 的影响，且影响后续绘制位置，相当于调整了画布的坐标
                Timber.d("layout 2 placeRelative-after")
            }

        }
        .backgroundCopy("2", Color.Green)
        .clickable {
            value += 1.dp
        })
}
