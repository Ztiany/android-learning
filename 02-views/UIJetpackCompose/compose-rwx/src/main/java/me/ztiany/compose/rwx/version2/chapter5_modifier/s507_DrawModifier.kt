package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun S507_DrawModifier() {
    Box(
        Modifier
            .padding(30.dp)
            .background(Color.Red)
            .drawWithContent {
                drawCircle(Color.Gray)
                drawContent()
            }
            .padding(20.dp)
            .background(Color.Green)
            .padding(10.dp)
            .background(Color.Blue)
            .size(40.dp)
    ) {

    }
}
