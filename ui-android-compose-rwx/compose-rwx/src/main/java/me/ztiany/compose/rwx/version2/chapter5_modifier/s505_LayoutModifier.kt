package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun S505_LayoutModifier() {
    Box(modifier = Modifier.background(Color.Green).padding(10.dp)) {
        Box(modifier = Modifier
            .requiredSize(80.dp)
            .background(Color.Red)
            .requiredSize(40.dp)) {

        }
    }
}