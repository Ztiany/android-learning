package me.ztiany.compose.foundation.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawRedDotScreen() {
    Column {
        DrawRedDotBehind(Modifier.weight(1F))
        DrawRedDotFront(Modifier.weight(1F))
    }
}