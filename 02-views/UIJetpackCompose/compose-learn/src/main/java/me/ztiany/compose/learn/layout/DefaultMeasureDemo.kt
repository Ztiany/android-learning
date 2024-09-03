package me.ztiany.compose.learn.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefaultMeasureDemo() {
    Box(
        Modifier
            .padding(16.dp)
            .background(color = Color.Blue)
            .size(100.dp)
    ) {
        Text(text = "Box1", color = Color.White)
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Color.Red)
        ) {
            Text(text = "Box2")
        }
    }
}