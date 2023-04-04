package me.ztiany.compose.foundation.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.unit.*
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun CenterByModifierExample() {
    Box(
        Modifier
            .fillMaxSizeCopy()
            .wrapContentSizeCopy()
            .size(50.dp)
            .background(Color.Blue)
            .clickable {
                parseModifier()
            }
    ) {

    }
}

fun Modifier.wrapContentSizeCopy(
    align: Alignment = Alignment.Center,
    unbounded: Boolean = false
) = this.then(createWrapContentSizeModifier(align, unbounded))

private fun createWrapContentSizeModifier(
    align: Alignment,
    unbounded: Boolean
) = WrapContentModifier(
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

private class WrapContentModifier(
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

private fun parseModifier() {
    Modifier
        .fillMaxSize()
        .wrapContentSize()
        .size(50.dp)
        .background(Color.Blue)
        .foldIn<Modifier>(Modifier) { acc, element ->
            Timber.d("element = $element")
            element
        }
}

@Stable
@Suppress("ModifierInspectorInfo")
fun Modifier.fillMaxSizeCopy() = this.then(createFillSizeModifier(1f))

private fun createFillSizeModifier(fraction: Float) = FillModifier(
    direction = Direction.Both,
    fraction = fraction,
    inspectorInfo = {
        name = "fillMaxSize"
        properties["fraction"] = fraction
    }
)

private enum class Direction {
    Vertical, Horizontal, Both
}

private class FillModifier(
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
            val width = (constraints.maxWidth * fraction).roundToInt()
                .coerceIn(constraints.minWidth, constraints.maxWidth)
            minWidth = width
            maxWidth = width
        } else {
            minWidth = constraints.minWidth
            maxWidth = constraints.maxWidth
        }
        val minHeight: Int
        val maxHeight: Int
        if (constraints.hasBoundedHeight && direction != Direction.Horizontal) {
            val height = (constraints.maxHeight * fraction).roundToInt()
                .coerceIn(constraints.minHeight, constraints.maxHeight)
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

    override fun equals(other: Any?) =
        other is FillModifier && direction == other.direction && fraction == other.fraction

    override fun hashCode() = direction.hashCode() * 31 + fraction.hashCode()

}