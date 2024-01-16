package me.ztiany.compose.learn.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun CenterByModifierExample() {
    var value by remember {
        mutableStateOf(50.dp)
    }

    Box(
        Modifier
            .fillMaxSizeCopy("1")
            .wrapContentSizeCopy("2")
            .onSizeChanged {
                Timber.d("onSizeChanged: $it")
            }
            .sizeCopy("3", value)
            .backgroundCopy("4", Color.Blue)
            .clickable {
                value += 1.dp
                parseModifier()
            }
    ) {

    }
}

private fun parseModifier() {
    Modifier
        .fillMaxSize()
        .wrapContentSize()
        .size(50.dp)
        .background(Color.Blue)
        .foldIn<Modifier>(Modifier) { _, element ->
            Timber.d("element = $element")
            element
        }
}
