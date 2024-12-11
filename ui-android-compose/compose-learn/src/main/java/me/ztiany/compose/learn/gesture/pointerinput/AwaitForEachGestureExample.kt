package me.ztiany.compose.learn.gesture.pointerinput

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AwaitForEachGestureExample() {
    val context = LocalContext.current
    SimpleClickable {
        Toast.makeText(
            context,
            "Clicked",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
private fun SimpleClickable(onClick: () -> Unit) {
    Box(
        Modifier
            .background(Color.Red)
            .size(100.dp)
            .pointerInput(onClick) {
                awaitEachGesture {
                    awaitFirstDown().also { it.consume() }
                    val up = waitForUpOrCancellation()
                    if (up != null) {
                        up.consume()
                        onClick()
                    }
                }
            }
    )
}
