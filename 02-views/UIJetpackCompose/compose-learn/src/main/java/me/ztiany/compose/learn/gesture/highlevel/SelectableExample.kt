package me.ztiany.compose.learn.gesture.highlevel


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectableScreen() {
    Column {
        SelectableExample()
        ToggleableExample()
    }
}

/**
 * 演示 Toggleable 的使用：<https://developer.android.com/reference/kotlin/androidx/compose/foundation/selection/package-summary#(androidx.compose.ui.Modifier).toggleable(kotlin.Boolean,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.foundation.Indication,kotlin.Boolean,androidx.compose.ui.semantics.Role,kotlin.Function1)>
 */
@Composable
private fun ToggleableExample() {
    var checked by remember { mutableStateOf(false) }
    Text(
        modifier = Modifier.toggleable(value = checked, onValueChange = { checked = it }),
        text = checked.toString()
    )
}

/**
 * 演示 Selectable 的使用：<https://developer.android.com/reference/kotlin/androidx/compose/foundation/selection/package-summary#(androidx.compose.ui.Modifier).selectable(kotlin.Boolean,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.foundation.Indication,kotlin.Boolean,androidx.compose.ui.semantics.Role,kotlin.Function0)>
 */
@Composable
private fun SelectableExample() {
    val option1 = Color.Red
    val option2 = Color.Blue
    var selectedOption by remember { mutableStateOf(option1) }

    Column {
        Text("Selected: $selectedOption")
        Row {
            listOf(option1, option2).forEach { color ->
                val selected = selectedOption == color
                Box(
                    Modifier
                        .size(100.dp)
                        .background(color = color)
                        .selectable(
                            selected = selected,
                            onClick = { selectedOption = color }
                        )
                )
            }
        }
    }
}