package me.ztiany.compose.foundation.modifiers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import timber.log.Timber


@Composable
fun OrderOfModifierExample() {
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
            layout(placement.width, placement.height) {//最终确定大小，所有的绘制都以这个大小为准。
                Timber.d("layout 1 placeRelative-before")
                placement.placeRelative(placement.width / 2, placement.height / 2)//影响后续绘制位置，相当于调整了画布的坐标
                Timber.d("layout 1 placeRelative-after")
            }
        }
        .backgroundCopy("1", Color.Blue)
        .size(200.dp)//size 具有强制性，第一次设置后，后面都不能再改了，除非你自定义测量过程。
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
        .backgroundCopy("2", Color.Green))
}

private fun Modifier.backgroundCopy(
    flag: String = "",
    color: Color,
    shape: Shape = RectangleShape
) = this.then(
    Background(
        flag,
        color = color,
        shape = shape,
        inspectorInfo = debugInspectorInfo {
            name = "background"
            value = color
            properties["color"] = color
            properties["shape"] = shape
        }
    )
)

private class Background constructor(
    private val flog: String = "",
    private val color: Color? = null,
    private val brush: Brush? = null,
    private val alpha: Float = 1.0f,
    private val shape: Shape,
    inspectorInfo: InspectorInfo.() -> Unit
) : DrawModifier, InspectorValueInfo(inspectorInfo) {

    // naive cache outline calculation if size is the same
    private var lastSize: Size? = null
    private var lastLayoutDirection: LayoutDirection? = null
    private var lastOutline: Outline? = null

    override fun ContentDrawScope.draw() {
        //这里的 flag 最后打印是因为 draw 阶段确实实在 measure/layout 之后。
        Timber.d("flag($flog) size: $size")
        if (shape === RectangleShape) {
            // shortcut to avoid Outline calculation and allocation
            drawRect()
        } else {
            drawOutline()
        }
        drawContent()
    }

    private fun ContentDrawScope.drawRect() {
        color?.let { drawRect(color = it) }
        brush?.let { drawRect(brush = it, alpha = alpha) }
    }

    private fun ContentDrawScope.drawOutline() {
        val outline =
            if (size == lastSize && layoutDirection == lastLayoutDirection) {
                lastOutline!!
            } else {
                shape.createOutline(size, layoutDirection, this)
            }
        color?.let { drawOutline(outline, color = color) }
        brush?.let { drawOutline(outline, brush = brush, alpha = alpha) }
        lastOutline = outline
        lastSize = size
        lastLayoutDirection = layoutDirection
    }

    override fun hashCode(): Int {
        var result = color?.hashCode() ?: 0
        result = 31 * result + (brush?.hashCode() ?: 0)
        result = 31 * result + alpha.hashCode()
        result = 31 * result + shape.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? Background ?: return false
        return color == otherModifier.color &&
                brush == otherModifier.brush &&
                alpha == otherModifier.alpha &&
                shape == otherModifier.shape
    }

    override fun toString(): String = "Background(color=$color, brush=$brush, alpha = $alpha, shape=$shape)"

}