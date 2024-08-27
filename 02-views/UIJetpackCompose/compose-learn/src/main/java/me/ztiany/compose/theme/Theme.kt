package me.ztiany.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF26527),
    secondary = Color(0xFFFC8C5A),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFF3F3F3),
)

private val LightAppColorScheme = AppColorScheme(
    lightest = Color.White,
    lightest90 = Color(0xE6FFFFFF),
    lightest80 = Color(0xCCFFFFFF),
    lightest70 = Color(0xB3FFFFFF),
    lightest60 = Color(0x99FFFFFF),
    lightest50 = Color(0x80FFFFFF),
    lightest40 = Color(0x66FFFFFF),
    lightest30 = Color(0x4DFFFFFF),
    lightest20 = Color(0x33FFFFFF),
    lightest10 = Color(0x1AFFFFFF),

    deepest = Color.White,
    deepest90 = Color(0xE6000000),
    deepest80 = Color(0xCC000000),
    deepest70 = Color(0xB3000000),
    deepest60 = Color(0x99000000),
    deepest50 = Color(0x80000000),
    deepest40 = Color(0x66000000),
    deepest30 = Color(0x4D000000),
    deepest20 = Color(0x33000000),
    deepest10 = Color(0x1A000000),

    textLevel1 = Color(0xFF333333),
    textLevel2 = Color(0xFF666666),
    textLevel3 = Color(0xFF999999),
    textLevel4 = Color(0xFFA4A4A4),
    textLevel5 = Color(0xFFCCCCCC),
    textLink = Color(0xFF5993F2),
    textStress = Color(0xFFF44766),
)

// TODO: implement dark color scheme
private val DarkAppColorScheme = LightAppColorScheme

private val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("No JetsnackColorPalette provided")
}

@Composable
fun ProvideAppColors(
    colors: AppColorScheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalAppColors provides colors, content = content)
}

object AppTheme {
    val colors: AppColorScheme
        @Composable
        get() = LocalAppColors.current
}

/**
 * For configure theme and migration, see:
 *
 *  - [Migrate XML themes to Compose](https://developer.android.com/develop/ui/compose/designsystems/views-to-compose)
 *  - [Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
 *  - [Migrate from Material 2 to Material 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material2-material3)
 *
 *  For customizing and extending your theme, refers to:
 *
 *  - [Jetsnack app](https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/theme/Theme.kt)
 */
@Composable
fun UIJetpackComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colors = if (darkTheme) DarkAppColorScheme else LightAppColorScheme

    ProvideAppColors(colors = colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}