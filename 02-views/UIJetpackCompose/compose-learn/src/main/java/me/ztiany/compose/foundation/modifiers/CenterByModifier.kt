package me.ztiany.compose.foundation.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun CenterByModifier() {
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize()
            .size(50.dp)
            .background(Color.Blue)
            .clickable {
                parseModifier()
            }
    ) {

    }
}

fun parseModifier() {
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
