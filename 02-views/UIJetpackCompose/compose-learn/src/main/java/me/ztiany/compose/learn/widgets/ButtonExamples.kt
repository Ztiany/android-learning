package me.ztiany.compose.learn.widgets

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/*
Button: Compose provides different types of Button according to the Material Design Buttons spec—Button, ElevatedButton, FilledTonalButton, OutlinedButton, and TextButton.

参考：
    1. https://jetpackcompose.cn/docs/elements/button
    2. https://jetpackcompose.cn/docs/elements/floatingactionbutton
    3. https://jetpackcompose.cn/docs/elements/iconbutton
 */
@Composable
fun ButtonExample(context: Context) {
    MaterialButtons()
    IconButtons()
    FloatingActionButtons()
}

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

@Composable
private fun IconButtons() {
    Row {
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Search, null)
        }
        IconButton(onClick = { }) {
            Icon(Icons.Filled.ArrowBack, null)
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
    content: @Composable () -> Unit
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

@Composable
private fun MaterialButtons() {
    Row {
        MaterialButtonExample()

        Spacer(modifier = Modifier.size(10.dp))

        CustomMaterialButtonExample()
    }
}

@Composable
private fun CustomMaterialButtonExample() {
    // 获取按钮的状态
    val interactionState = remember { MutableInteractionSource() }

    val (text, textColor, buttonColor) = when {
        interactionState.collectIsPressedAsState().value -> ButtonState("Just Pressed", Color.Red, Color.Black)
        else -> ButtonState("Just Button", Color.White, Color.Red)
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

@Composable
private fun MaterialButtonExample() {
    Button(
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
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Like")
    }
}

private data class ButtonState(var text: String, var textColor: Color, var buttonColor: Color)
