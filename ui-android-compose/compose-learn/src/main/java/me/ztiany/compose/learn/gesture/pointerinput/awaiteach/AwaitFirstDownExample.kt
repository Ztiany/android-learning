package me.ztiany.compose.learn.gesture.pointerinput.awaiteach

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


/** 演示 awaitFirstDown 的使用 */
@Composable
fun AwaitFirstDownViews() {
    /* awaitFirstDown 将等待第一根手指 ACTION_DOWN 事件时恢复执行，并将手指按下事件返回。 */
    var result by remember {
        mutableStateOf("")
    }

    Column {
        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Green, RoundedCornerShape(10.dp))
                .size(250.dp)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val awaitFirstDown = awaitFirstDown()
                        result = awaitFirstDown.position.toString()
                    }
                }) {

        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = result)
    }
}
