package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun S707_2DimensionScrollObservation() {
    Modifier.pointerInput(Unit){
        detectDragGestures { change, dragAmount ->

        }
    }
}