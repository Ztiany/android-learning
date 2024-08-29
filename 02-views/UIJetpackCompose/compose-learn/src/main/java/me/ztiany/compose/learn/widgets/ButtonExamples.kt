package me.ztiany.compose.learn.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
Button: Compose provides different types of Button according to the Material Design Buttons spec—
Button, ElevatedButton, FilledTonalButton, OutlinedButton, and TextButton.

For widget, refer to:

1. https://jetpackcompose.cn/docs/elements/button
2. https://jetpackcompose.cn/docs/elements/floatingactionbutton
3. https://jetpackcompose.cn/docs/elements/iconbutton

Customizing the button's appearance and  colors?

1. [button color](https://stackoverflow.com/questions/64376333/background-color-on-button-in-jetpack-compose)
2. [gradient button](https://stackoverflow.com/questions/65542068/jetpack-compose-button-with-gradient-background)

For interaction, refer to:

1. https://developer.android.com/develop/ui/compose/touch-input/user-interactions/handling-interactions
2. https://medium.com/androiddevelopers/illuminating-interactions-visual-state-in-jetpack-compose-188fa041b791

As of 1.7.0, RippleTheme has been deprecated. For more information, refer to:

1. [What is RippleTheme and how to use it?](https://medium.com/@fergus.a.hewson/compose-ripple-customisation-guide-5cb11e4ad876)
2. [Migrate to Indication and Ripple APIs](https://developer.android.com/develop/ui/compose/touch-input/user-interactions/migrate-indication-ripple)
 */
@Composable
fun ButtonExample() {
    Column {
        NormalButtons()
        GradientButtons()
        IconButtons()
        FloatingActionButtons()
        Indications()
    }
}

///////////////////////////////////////////////////////////////////////////
// normal buttons
///////////////////////////////////////////////////////////////////////////
@Composable
private fun NormalButtons() {
    Row {
        MaterialButton(true)
        Spacer(modifier = Modifier.size(5.dp))
        MaterialButton(false)
        Spacer(modifier = Modifier.size(5.dp))
        CustomMaterialButton()
    }

}

@Composable
private fun MaterialButton(enabled: Boolean = true) {
    Button(
        enabled = enabled,
        // define button's normal and disable color.
        colors = ButtonDefaults.buttonColors(),
        onClick = {
            /* ... */
        },
        // Uses ButtonDefaults.ContentPadding by default
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        // Inner content including an icon and a text label
        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Like")
    }
}

private data class ButtonState(
    var text: String,
    var textColor: Color,
    var buttonColor: Color,
)

@Composable
private fun CustomMaterialButton() {
    // 获取按钮的状态
    val interactionState = remember { MutableInteractionSource() }

    val (text, textColor, buttonColor) = when {
        interactionState.collectIsPressedAsState().value -> ButtonState("Pressed", Color.Red, Color.Black)
        else -> ButtonState("Button", Color.White, Color.Red)
    }

    Button(
        onClick = {
            /* ... */
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
        ),
        interactionSource = interactionState,
        // Uses ButtonDefaults.ContentPadding by default
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        // Inner content including an icon and a text label
        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = textColor
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = text, color = textColor)
    }
}

///////////////////////////////////////////////////////////////////////////
// gradient buttons
///////////////////////////////////////////////////////////////////////////

@Composable
private fun GradientButtons() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Blue,
                        Color.Green,
                    )
                ),
                shape = ButtonDefaults.shape,
            )
            .height(ButtonDefaults.MinHeight),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = {},
    ) {
        Text("Button content")
    }

    Spacer(modifier = Modifier.size(5.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Red, ButtonDefaults.shape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Blue,
                        Color.Green,
                    )
                ),
                shape = ButtonDefaults.shape,
            )
            .height(ButtonDefaults.MinHeight),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = {},
    ) {
        Text("Button content")
    }
}

///////////////////////////////////////////////////////////////////////////
// icon buttons
///////////////////////////////////////////////////////////////////////////

@Composable
private fun IconButtons() {
    Row {
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Search, null)
        }
        IconButton(onClick = { }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Done, null)
        }
        NoRippleIconButton(onClick = { }) {
            Icon(Icons.Filled.Home, null)
        }
    }
}

@Composable
private fun NoRippleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            )
            .then(Modifier.size(48.dp)),
        contentAlignment = Alignment.Center
    ) { content() }
}

///////////////////////////////////////////////////////////////////////////
// floating action buttons
///////////////////////////////////////////////////////////////////////////

@Composable
private fun FloatingActionButtons() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        FloatingActionButton(onClick = { /*do something*/ }) {
            Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
        }

        Spacer(modifier = Modifier.size(10.dp))

        ExtendedFloatingActionButton(
            onClick = { /* ... */ },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Favorite"
                )
            },
            text = { Text("Like") }
        )
    }
}

///////////////////////////////////////////////////////////////////////////
// indications
///////////////////////////////////////////////////////////////////////////

@Composable
private fun Indications() {
    PressIconButton(
        onClick = {},
        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
        text = { Text("Add to Cart!") }
    )
}

@Composable
private fun PressIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed = interactionSource.collectIsPressedAsState().value
    Button(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
    ) {
        AnimatedVisibility(visible = isPressed) {
            if (isPressed) {
                Row {
                    icon()
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                }
            }
        }
        text()
    }
}

// as from 1.7.0.
private class ScaleNode(private val interactionSource: InteractionSource) : Modifier.Node(), DrawModifierNode {

    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1F)

    private suspend fun animateToPressed(pressPosition: Offset) {
        currentPressPosition = pressPosition
        animatedScalePercent.animateTo(0.9f, spring())
    }

    private suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, spring())
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToResting()
                    is PressInteraction.Cancel -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }

}