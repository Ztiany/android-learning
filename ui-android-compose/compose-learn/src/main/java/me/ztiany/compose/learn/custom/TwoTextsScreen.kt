package me.ztiany.compose.learn.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TwoTextsScreen() {
    Column {
        TwoTextsExampleByRow(Modifier.weight(1F))
        TwoTextsExampleByCustom(Modifier.weight(1F))
        TwoTextsExampleBySubcomposeLayout(Modifier.weight(1F))
    }
}