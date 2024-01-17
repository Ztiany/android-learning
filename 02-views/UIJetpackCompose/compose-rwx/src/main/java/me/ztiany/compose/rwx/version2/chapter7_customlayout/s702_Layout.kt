package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun S702_Layout() {
    CustomLayout {
        Box(
            Modifier
                .size(80.dp)
                .background(Color.Red))
        Box(
            Modifier
                .size(80.dp)
                .background(Color.Yellow))
        Box(
            Modifier
                .size(80.dp)
                .background(Color.Blue))
    }

}

@Composable
private fun CustomLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content, modifier) { measurables, constraints ->
        var width = 0
        var height = 0
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints).also { placeable ->
                width = max(width, placeable.width)
                height += placeable.height
            }
        }
        layout(width, height) {
            var totalHeight = 0
            placeables.forEach {
                it.placeRelative(0, totalHeight)
                totalHeight += it.height
            }
        }
    }
}