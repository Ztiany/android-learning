package me.ztiany.compose.foundation.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import me.ztiany.compose.R
import kotlin.math.roundToInt

@Composable
fun WaveLoadingDemo() {
    var _progress by remember { mutableStateOf(0.5f) }
    var _velocity by remember { mutableStateOf(1.0f) }
    var _amplitude by remember { mutableStateOf(0.2f) }

    val size = LocalDensity.current.run {
        200.dp.toPx().roundToInt()
    }

    val _bitmap = ImageBitmap.imageResource(id = R.drawable.logo_nba)
        .asAndroidBitmap().scale(size, size)

    Column {
        Box(
            Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            WaveLoading(
                Modifier
                    .size(200.dp)
                    .clipToBounds()
                    .align(Alignment.Center),
                WaveConfig(_progress, _amplitude, _velocity),
                bitmap = _bitmap
            )

        }

        LabelSlider(
            label = "Progress",
            value = _progress,
            onValueChange = { _progress = it },
            range = 0f..1f
        )

        LabelSlider(
            label = "Velocity",
            value = _velocity,
            onValueChange = { _velocity = it },
            range = 0f..1f
        )

        LabelSlider(
            label = "Amplitude",
            value = _amplitude,
            onValueChange = { _amplitude = it },
            range = 0f..1f
        )
    }
}


@Composable
private fun LabelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Row(Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            label, modifier = Modifier
                .width(100.dp)
                .align(Alignment.CenterVertically)
        )
        Slider(
            modifier = Modifier.align(Alignment.CenterVertically),
            value = value,
            onValueChange = onValueChange,
            valueRange = range
        )
    }
}