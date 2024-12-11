package me.ztiany.compose.learn.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.composableWithSlidingAnimation

private const val ROUTE_NAME = "theme_route"

fun NavController.navigateToTheme() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.themeScreen(navController: NavHostController) {
    composableWithSlidingAnimation(ROUTE_NAME) {
        ThemeScreen(navController)
    }
}

/**
 * 掌握 MaterialTheme3 中的颜色和字体。除此之外，还有 Shape、Elevation、Typography 等。
 *
 * 关于颜色，有一个重要的函数：contentColorFor，用于根据背景色自动选择合适的文本颜色。
 * 如果没有找到合适的颜色则会使用 LocalContentColor 定义的颜色。也是就说，我们可以通过
 * LocalContentColor 来定义默认的文本颜色。
 */
@Composable
private fun ThemeScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
    ) {

        ColorArea("primary", MaterialTheme.colorScheme.primary) {
            TypographyText()
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
                Text("onPrimary")
            }
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
                Text("inversePrimary")
            }
        }
        ColorArea("primaryContainer", MaterialTheme.colorScheme.primaryContainer) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("primary", color = MaterialTheme.colorScheme.primary)
            }
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("onPrimaryContainer", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        ColorArea("secondary", MaterialTheme.colorScheme.secondary) {
            Text("onSecondary", color = MaterialTheme.colorScheme.onSecondary)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }
        }
        ColorArea("secondaryContainer", MaterialTheme.colorScheme.secondaryContainer) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("secondary", color = MaterialTheme.colorScheme.secondary)
            }
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.onSecondaryContainer)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("onSecondaryContainer", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }

        ColorArea("tertiary", MaterialTheme.colorScheme.tertiary) {
            Text("onTertiary", color = MaterialTheme.colorScheme.onTertiary)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.onTertiary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }
        }
        ColorArea("tertiaryContainer", MaterialTheme.colorScheme.tertiaryContainer) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("tertiary", color = MaterialTheme.colorScheme.tertiary)
            }
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.onTertiaryContainer)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("onTertiaryContainer", color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }

        ColorArea("error", MaterialTheme.colorScheme.error) {
            Text("onError", color = MaterialTheme.colorScheme.onError)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.onError)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }
        }
        ColorArea("errorContainer", MaterialTheme.colorScheme.errorContainer) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.error)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("error", color = MaterialTheme.colorScheme.error)
            }
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.onErrorContainer)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("onErrorContainer", color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }

        ColorArea("background", MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("onBackground", color = MaterialTheme.colorScheme.onBackground)
            }
        }

        ColorArea("surface", MaterialTheme.colorScheme.surface) {
            Text("inverseSurface", color = MaterialTheme.colorScheme.inverseSurface)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
                Text("inverseOnSurface", color = MaterialTheme.colorScheme.inverseOnSurface)
            }

            Text("surfaceVariant", color = MaterialTheme.colorScheme.surfaceVariant)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
                Text("onSurfaceVariant", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Text("surfaceBright", color = MaterialTheme.colorScheme.surfaceBright)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceBright)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }

            Text("surfaceDim", color = MaterialTheme.colorScheme.surfaceDim)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }

            Text("onSurface", color = MaterialTheme.colorScheme.onSurface)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.onSurface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }

            Text("surfaceTint", color = MaterialTheme.colorScheme.surfaceTint)
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
            }
        }
        ColorArea("surfaceContainerLowest", MaterialTheme.colorScheme.surfaceContainerLowest) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
            }
        }
        ColorArea("surfaceContainerLow", MaterialTheme.colorScheme.surfaceContainerLow) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
            }
        }
        ColorArea("surfaceContainer", MaterialTheme.colorScheme.surfaceContainer) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
            }
        }
        ColorArea("surfaceContainerHigh", MaterialTheme.colorScheme.surfaceContainerHigh) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
            }
        }
        ColorArea("surfaceContainerHigh", MaterialTheme.colorScheme.surfaceContainerHigh) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
            }
        }

        ColorArea("outline", MaterialTheme.colorScheme.outline) {}
        ColorArea("outlineVariant", MaterialTheme.colorScheme.outlineVariant) {}

        ColorArea("shapes on primary", MaterialTheme.colorScheme.primary) {
            Text("Defined in ShapeTokens.kt")
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.extraLarge)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("extraLarge, 28.dp")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("large, 16.dp")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("medium, 12.dp")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.small)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("small, 8.dp")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.extraSmall)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("extraSmall, 4.dp")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("CircleShape, the default shape of buttons.")
            }
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = RectangleShape)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("RectangleShape")
            }
        }
    }
}

@Composable
private fun TypographyText() {
    Text("Defined in TypographyTokens.kt")
    Spacer(Modifier.height((20.dp)))

    Text("""
     display:
        57.sp
        45.sp
        36.sp
    """.trimIndent())
    Text(
        text = "I am a text with onPrimary color and displayLarge typography",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.displayLarge
    )
    Text(
        text = "I am a text with onPrimary color and displayMedium typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.displayMedium
    )
    Text(
        text = "I am a text with onPrimary color and displaySmall typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.displaySmall
    )

    Spacer(Modifier.height((20.dp)))
    Text("""
     headline:
        32.sp
        28.sp
        24.sp
    """.trimIndent())
    Text(
        text = "I am a text with onPrimary color and headlineLarge typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineLarge
    )
    Text(
        text = "I am a text with onPrimary color and headlineMedium typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = "I am a text with onPrimary color and headlineSmall typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineSmall
    )

    Spacer(Modifier.height((20.dp)))
    Text("""
     title:
        22.sp
        5.sp
        14.sp
    """.trimIndent())
    Text(
        text = "I am a text with onPrimary color and titleLarge typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = "I am a text with onPrimary color and titleMedium typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleMedium
    )
    Text(
        text = "I am a text with onPrimary color and titleSmall typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleSmall
    )

    Spacer(Modifier.height((20.dp)))
    Text("""
     body:
        16.sp
        14.sp
        12.sp
    """.trimIndent())
    Text(
        text = "I am a text with onPrimary color and bodyLarge typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = "I am a text with onPrimary color and bodyMedium typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        text = "I am a text with onPrimary color and bodySmall typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(Modifier.height((20.dp)))
    Text("""
     label:
        14.sp
        12.sp
        11.sp
    """.trimIndent())
    Text(
        text = "I am a text with onPrimary color and labelLarge typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelLarge
    )
    Text(
        text = "I am a text with onPrimary color and labelMedium typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelMedium
    )
    Text(
        text = "I am a text with onPrimary color and labelSmall typography.",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun ColorArea(name: String, color: Color, content: @Composable ColumnScope.() -> Unit) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(10.dp)
    )
    Column(
        Modifier
            .background(color = color)
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(10.dp)
    ) {
        content()
    }
}