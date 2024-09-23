package me.ztiany.compose.learn.modifier

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
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facility.debug.backgroundCopy
import timber.log.Timber

@Composable
fun BackgroundSizeSample() {
    var value by remember {
        mutableStateOf(200.dp)
    }

    /*
        结论：
            1. measure 的发起点是顶部的 LayoutModifier，终点是 measurePolicy，然后是从顶部一直传递到底部，
                再从底部依次执行 layout 向上返回测量结果。因此，measure 的影响是顶从底部到顶部的，某个 DrawModifier
                受到其下游最近的 measure 的影响。
            2. place 的顺序是也是一样。但是产生的影响是顶部到底下。最先产生影响的是顶部，每次 place 的效果都会叠加。
    */
    Layout({

    }, measurePolicy = { placeables, constraints ->
        //测量链条的末端，返回给 2
        Timber.d("measurePolicy placeables: ${placeables.size}")
        Timber.d("measurePolicy constraints: $constraints")
        Timber.d("measurePolicy layout: ${constraints.minWidth / 3}x${constraints.minHeight / 3}")
        layout(constraints.minWidth / 3, constraints.minHeight / 3) {
            Timber.d("measurePolicy layout")
        }

    }, modifier = Modifier
        .backgroundCopy("Blue", Color.Blue)
        .size(value)
        // 在中间绘制了一个 1/3 的 红色方框：因为画布的裁减是从中间开始的。
        .backgroundCopy("Red", Color.Red)
        .clickable {
            value += 1.dp
        })
}