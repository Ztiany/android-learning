package me.ztiany.compose.learn.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun PopupScreen() {
    val list = listOf("1", "2", "3")
    var showPopup by rememberSaveable { mutableStateOf(false) }
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .padding(100.dp)
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.Yellow)
                .border(1.dp, Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            list.onEachIndexed { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .clickable {
                            showPopup = true
                            selectedIndex = index
                        }
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Divider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }

    PopupBox(
        popupWidth = 200F,
        popupHeight = 30F,
        showPopup = showPopup,
        onClickOutside = { showPopup = false },
        content = { Text("hello ${list[selectedIndex]}") })
}

@Composable
private fun PopupBox(
    popupWidth: Float,
    popupHeight: Float,
    showPopup: Boolean,
    onClickOutside: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (showPopup) {
        // popup
        Popup(
            // 这里的 alignment 是相对于 parent 的位置，与传统意义上的 PopupWindow 还是有一定的差距的。
            alignment = Alignment.TopStart,
            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),
            // to dismiss on click outside
            onDismissRequest = { onClickOutside() }
        ) {
            Box(
                Modifier
                    .width(popupWidth.dp)
                    .height(popupHeight.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}