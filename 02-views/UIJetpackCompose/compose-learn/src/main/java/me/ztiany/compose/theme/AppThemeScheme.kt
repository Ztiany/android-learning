package me.ztiany.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColorScheme(
    val lightest: Color,
    val deepest: Color,

    val textLevel1: Color,
    val textLevel2: Color,
    val textLevel3: Color,
    val textLevel4: Color,
    val textLevel5: Color,
    val textLink: Color,
    val textStress: Color,
)