package me.ztiany.compose.foundation.custom

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->

    // Measure the composable
    // 通过调用 measurable.measure(constraints) 来测量以可测量参数表示的 Text。
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]
    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY

    Log.d("firstBaselineToTop", "firstBaseline = $firstBaseline")
    Log.d("firstBaselineToTop", "placeableY = $placeableY")
    Log.d("firstBaselineToTop", "height = $height")

    //通过调用 layout(width, height) 方法指定可组合项的尺寸，该方法还会提供一个用于放置被封装元素的 lambda。
    layout(placeable.width, height) {
        // Where the composable gets placed
        //通过调用 placeable.place(x, y) 将被封装的元素放到屏幕上。如果未放置被封装的元素，它们将不可见。
        placeable.placeRelative(0, placeableY)
    }
}

@Composable
fun Example01() {
    Row {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
        Text("Hi there!")
    }
}