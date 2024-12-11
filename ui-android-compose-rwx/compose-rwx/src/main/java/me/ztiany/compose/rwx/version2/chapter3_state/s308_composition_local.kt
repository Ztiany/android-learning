package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// 规范：以 local/Local 为前缀。localXxx 可用于模块内共享，LocalXxx 可用于全局共享。
private val localName = compositionLocalOf {
    "Name"
}

/*
compositionLocalOf 与 staticCompositionLocalOf 的区别？

    - 如果 localBackground 是用 compositionLocalOf 创建的，那么修改 themeColor 的时候，只有第二层的 TextWithBackground 会被重组，第一层和第三层的都不会，因为在使用 themeColor 的时候做了记录，可以智能地做到最小范围重组。
    - 如果 localBackground 是用 staticCompositionLocalOf 创建的，那么修改 themeColor 的时候，第二层以及其所有的子层都会被重组，只有第一层不会，因为使用 themeColor 的时候不做记录。

如何选择？当一个 State 频繁更新，应该使用 compositionLocalOf ，否正应该使用 staticCompositionLocalOf。

 */
private val localBackground = compositionLocalOf { Color.Red }
//private val localBackground = staticCompositionLocalOf { Color.Red }

private val themeColor = mutableStateOf(Color.Black)

// 通过 CompositionLocal 可以在 Composable 间共享数据。
@Composable
fun S308_SharedDataBetweenComposable() {
    CompositionLocalProvider(localName provides "Alien") {
        Detail()
    }

    CompositionLocalProvider(localBackground provides Color.Blue) {
        TextWithBackground()
        CompositionLocalProvider(localBackground provides themeColor.value) {
            TextWithBackground()
            CompositionLocalProvider(localBackground provides Color.Gray) {
                TextWithBackground()
            }
        }
    }
}

@Composable
private fun TextWithBackground() {
    Text(text = "Haha", modifier = Modifier.background(localBackground.current))
}

@Composable
private fun Detail() {
    Text(text = localName.current)
}