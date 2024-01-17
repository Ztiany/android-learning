package me.ztiany.compose.rwx.facility

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SimpleScaffold(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = title) })
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()
        }
    }
}