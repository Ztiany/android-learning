package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun S501_WhatIsModifier() {
    Custom(Modifier.fillMaxSize())
}

/* Kotlin 官方推荐将 Composable 的第一个参数设置为 Modifier */
@Composable
private fun Custom(modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(text = "Hello World", modifier = modifier)
    }
}