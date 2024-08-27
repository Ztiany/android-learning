package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import timber.log.Timber

/*
    OnGloballyPositionModifier 于 OnPlaceModifier 类似，但是它是全局的。
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun S518_OnGloballyPositionModifier() {
    var size by remember {
        mutableStateOf(200.dp)
    }
    Column {
        Box(
            Modifier
                .onGloballyPositioned {
                    Timber.d("onGloballyPositioned 2: ${it.size}")
                }
                .size(size)
        ) {

        }

        Button(onClick = {
            size += 15.dp
        }) {
            Text(text = "Change Size2")
        }
    }
}