package me.ztiany.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColorScheme(
    val lightest: Color,
    val lightest90: Color,
    val lightest80: Color,
    val lightest70: Color,
    val lightest60: Color,
    val lightest50: Color,
    val lightest40: Color,
    val lightest30: Color,
    val lightest20: Color,
    val lightest10: Color,

    val deepest: Color,
    val deepest90: Color,
    val deepest80: Color,
    val deepest70: Color,
    val deepest60: Color,
    val deepest50: Color,
    val deepest40: Color,
    val deepest30: Color,
    val deepest20: Color,
    val deepest10: Color,

    val textLevel1: Color,
    val textLevel2: Color,
    val textLevel3: Color,
    val textLevel4: Color,
    val textLevel5: Color,
    val textLink: Color,
    val textStress: Color,
)