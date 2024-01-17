package me.ztiany.compose.facility.debug

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrain
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import timber.log.Timber
import kotlin.math.roundToInt

/*
    用于 Debug 的 Modifier，可以在 Modifier 中添加 flag，方便在日志中查看 Modifier 的执行顺序。
*/

fun Modifier.backgroundCopy(
    flag: String,
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

@Stable
@Suppress("ModifierInspectorInfo")
fun Modifier.fillMaxSizeCopy(flag: String) = this.then(createFillSizeModifier(flag, 1F))

@SuppressLint("ModifierFactoryExtensionFunction", "ModifierFactoryReturnType")
private fun createFillSizeModifier(flag: String, fraction: Float) = FillModifier(
    flag = flag,
    direction = Direction.Both,
    fraction = fraction,
    inspectorInfo = {
        name = "fillMaxSize"
        properties["fraction"] = fraction
    }
)

fun Modifier.wrapContentSizeCopy(
    flag: String,
    align: Alignment = Alignment.Center,
    unbounded: Boolean = false
) = this.then(createWrapContentSizeModifier(flag, align, unbounded))

@SuppressLint("ModifierFactoryExtensionFunction", "ModifierFactoryReturnType")
private fun createWrapContentSizeModifier(
    flag: String,
    align: Alignment,
    unbounded: Boolean
) = WrapContentModifier(
    flag = flag,
    direction = Direction.Both,
    unbounded = unbounded,
    alignmentCallback = { size, layoutDirection ->
        align.align(IntSize.Zero, size, layoutDirection)
    },
    align,
    inspectorInfo = {
        name = "wrapContentSize"
        properties["align"] = align
        properties["unbounded"] = unbounded
    }
)

@Stable
fun Modifier.sizeCopy(flag: String, size: Dp) = this.then(
    SizeModifier(
        flag = flag,
        minWidth = size,
        maxWidth = size,
        minHeight = size,
        maxHeight = size,
        enforceIncoming = true,
        inspectorInfo = debugInspectorInfo {
            name = "size"
            value = size
        }
    )
)

///////////////////////////////////////////////////////////////////////////
// Impl
///////////////////////////////////////////////////////////////////////////

private class WrapContentModifier(
    private val flag: String = "",
    private val direction: Direction,
    private val unbounded: Boolean,
    private val alignmentCallback: (IntSize, LayoutDirection) -> IntOffset,
    private val align: Any, // only used for equals and hashcode
    inspectorInfo: InspectorInfo.() -> Unit
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val wrappedConstraints = Constraints(
            minWidth = if (direction != Direction.Vertical) 0 else constraints.minWidth,
            minHeight = if (direction != Direction.Horizontal) 0 else constraints.minHeight,
            maxWidth = if (direction != Direction.Vertical && unbounded) {
                Constraints.Infinity
            } else {
                constraints.maxWidth
            },
            maxHeight = if (direction != Direction.Horizontal && unbounded) {
                Constraints.Infinity
            } else {
                constraints.maxHeight
            }
        )
        val placeable = measurable.measure(wrappedConstraints)
        val wrapperWidth = placeable.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        val wrapperHeight = placeable.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        return layout(
            wrapperWidth,
            wrapperHeight
        ) {
            val position = alignmentCallback(
                IntSize(wrapperWidth - placeable.width, wrapperHeight - placeable.height),
                layoutDirection
            )
            placeable.place(position)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is WrapContentModifier) return false
        return direction == other.direction && unbounded == other.unbounded && align == other.align
    }

    override fun hashCode() =
        (direction.hashCode() * 31 + unbounded.hashCode()) * 31 + align.hashCode()
}

private enum class Direction {
    Vertical, Horizontal, Both
}

private class FillModifier(
    val flag: String = "",
    private val direction: Direction,
    private val fraction: Float,
    inspectorInfo: InspectorInfo.() -> Unit
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val minWidth: Int
        val maxWidth: Int
        if (constraints.hasBoundedWidth && direction != Direction.Vertical) {
            val width = (constraints.maxWidth * fraction).roundToInt().coerceIn(constraints.minWidth, constraints.maxWidth)
            minWidth = width
            maxWidth = width
        } else {
            minWidth = constraints.minWidth
            maxWidth = constraints.maxWidth
        }
        val minHeight: Int
        val maxHeight: Int
        if (constraints.hasBoundedHeight && direction != Direction.Horizontal) {
            val height = (constraints.maxHeight * fraction).roundToInt().coerceIn(constraints.minHeight, constraints.maxHeight)
            minHeight = height
            maxHeight = height
        } else {
            minHeight = constraints.minHeight
            maxHeight = constraints.maxHeight
        }

        val placeable = measurable.measure(
            Constraints(minWidth, maxWidth, minHeight, maxHeight)
        )

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun equals(other: Any?) = other is FillModifier && direction == other.direction && fraction == other.fraction

    override fun hashCode() = direction.hashCode() * 31 + fraction.hashCode()

}

private class Background constructor(
    private val flag: String = "",
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
        Timber.d("flag($flag) size: $size")
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

    override fun toString(): String = "Background(flag=${flag}, color=$color, brush=$brush, alpha = $alpha, shape=$shape)"

}


private class SizeModifier(
    val flag: String = "",
    private val minWidth: Dp = Dp.Unspecified,
    private val minHeight: Dp = Dp.Unspecified,
    private val maxWidth: Dp = Dp.Unspecified,
    private val maxHeight: Dp = Dp.Unspecified,
    private val enforceIncoming: Boolean,
    inspectorInfo: InspectorInfo.() -> Unit
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
    private val Density.targetConstraints: Constraints
        get() {
            val maxWidth = if (maxWidth != Dp.Unspecified) {
                maxWidth.coerceAtLeast(0.dp).roundToPx()
            } else {
                Constraints.Infinity
            }
            val maxHeight = if (maxHeight != Dp.Unspecified) {
                maxHeight.coerceAtLeast(0.dp).roundToPx()
            } else {
                Constraints.Infinity
            }
            val minWidth = if (minWidth != Dp.Unspecified) {
                minWidth.roundToPx().coerceAtMost(maxWidth).coerceAtLeast(0).let {
                    if (it != Constraints.Infinity) it else 0
                }
            } else {
                0
            }
            val minHeight = if (minHeight != Dp.Unspecified) {
                minHeight.roundToPx().coerceAtMost(maxHeight).coerceAtLeast(0).let {
                    if (it != Constraints.Infinity) it else 0
                }
            } else {
                0
            }
            return Constraints(
                minWidth = minWidth,
                minHeight = minHeight,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val wrappedConstraints = targetConstraints.let { targetConstraints ->
            if (enforceIncoming) {
                constraints.constrain(targetConstraints)
            } else {
                val resolvedMinWidth = if (minWidth != Dp.Unspecified) {
                    targetConstraints.minWidth
                } else {
                    constraints.minWidth.coerceAtMost(targetConstraints.maxWidth)
                }
                val resolvedMaxWidth = if (maxWidth != Dp.Unspecified) {
                    targetConstraints.maxWidth
                } else {
                    constraints.maxWidth.coerceAtLeast(targetConstraints.minWidth)
                }
                val resolvedMinHeight = if (minHeight != Dp.Unspecified) {
                    targetConstraints.minHeight
                } else {
                    constraints.minHeight.coerceAtMost(targetConstraints.maxHeight)
                }
                val resolvedMaxHeight = if (maxHeight != Dp.Unspecified) {
                    targetConstraints.maxHeight
                } else {
                    constraints.maxHeight.coerceAtLeast(targetConstraints.minHeight)
                }
                Constraints(
                    resolvedMinWidth,
                    resolvedMaxWidth,
                    resolvedMinHeight,
                    resolvedMaxHeight
                )
            }
        }
        val placeable = measurable.measure(wrappedConstraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedWidth) {
            constraints.maxWidth
        } else {
            constraints.constrainWidth(measurable.minIntrinsicWidth(height))
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedHeight) {
            constraints.maxHeight
        } else {
            constraints.constrainHeight(measurable.minIntrinsicHeight(width))
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedWidth) {
            constraints.maxWidth
        } else {
            constraints.constrainWidth(measurable.maxIntrinsicWidth(height))
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedHeight) {
            constraints.maxHeight
        } else {
            constraints.constrainHeight(measurable.maxIntrinsicHeight(width))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SizeModifier) return false
        return minWidth == other.minWidth &&
                minHeight == other.minHeight &&
                maxWidth == other.maxWidth &&
                maxHeight == other.maxHeight &&
                enforceIncoming == other.enforceIncoming
    }

    override fun hashCode() =
        (
                (
                        (((minWidth.hashCode() * 31 + minHeight.hashCode()) * 31) + maxWidth.hashCode()) *
                                31
                        ) + maxHeight.hashCode()
                ) * 31
}
