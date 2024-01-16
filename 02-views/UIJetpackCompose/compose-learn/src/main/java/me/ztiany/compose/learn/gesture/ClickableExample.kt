package me.ztiany.compose.learn.gesture

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 演示 Clickable 和 CombinedClickable 的使用*/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickableViews() {

    /*
    最佳实践：在处理手势时，应将手势处理修饰符尽可能放到 Modifier 末尾，从而可以避免产生不可预期的行为。
     */
    Column(Modifier.fillMaxSize()) {

        var result by remember {
            mutableStateOf("")
        }

        val combinedClickState = remember {
            mutableStateOf(true)
        }

        Text(text = result)

        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Blue, RoundedCornerShape(10.dp))
                .height(50.dp)
                .fillMaxWidth()
                // Clickable 修饰符用来监听组件的点击操作，并且当点击事件发生时，会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                .clickable(true) {
                    result = "蓝色 Box 被点击了"
                }
        ) {
            Text(text = "操作我", Modifier.align(Alignment.Center))
        }

        Box(
            Modifier
                .padding(20.dp)
                .background(Color.Red, RoundedCornerShape(10.dp))
                .height(50.dp)
                .fillMaxWidth()
                //对于长按点击、双击等复合类点击手势，可以使用 CombinedClickable 修饰符来实现手势监听。与 Clickable 修饰符一样，其同样也可以监听单击手势，并且也会为被点击的组件施加一个波纹涟漪效果动画的蒙层。
                .combinedClickable(combinedClickState.value,
                    onLongClick = {
                        result = "红色 Box 被长按了"
                    },
                    onDoubleClick = {
                        result = "红色 Box 被双击了"
                    },
                    onClick = {
                        result = "红色 Box 被点击了"
                    }
                )
        ) {
            Text(text = "操作我", Modifier.align(Alignment.Center))
        }

    }

}