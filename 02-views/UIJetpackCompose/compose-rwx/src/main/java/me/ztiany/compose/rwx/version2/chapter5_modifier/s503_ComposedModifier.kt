package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
    通过 composed，实现有状态的 Modifier。
 */
@Composable
fun S503_ComposedModifier() {
    val composedModifier = Modifier.composed {
        var padding by remember {
            mutableStateOf(10.dp)
        }
        Modifier
            .padding(padding)
            .clickable {
                padding = 0.dp
            }
    }

    var padding by remember {
        mutableStateOf(10.dp)
    }

    val normalModifier = Modifier
        .padding(padding)
        .clickable {
            padding = 0.dp
        }

    Row {
        Column {
            Box(Modifier.background(Color.Red) then normalModifier)
            Text(text = "Hello World", modifier = Modifier.background(Color.Green) then normalModifier)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Box(Modifier.background(Color.Red) then composedModifier)
            Text(text = "Hello World", modifier = Modifier.background(Color.Green) then composedModifier)
        }
    }

}