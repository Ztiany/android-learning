package me.ztiany.compose.learn.animation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 演示 Modifier.AnimateContentSize 的使用 */
@Composable
fun AnimateContentSizeBox() {
    Column {
        var expended by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .padding(5.dp)
                .background(Color.Blue, shape = RoundedCornerShape(5.dp))
                .padding(5.dp)
                // animateContentSize 是一个 Modifier 修饰符方法。它的用途非常专一，当容器尺寸发生变化时，会通过动画进行过渡，开箱即用。
                // 这个实例中，expend 决定文本的最大行数，也就决定了 Box 的整体尺寸，正常情况下大小的变化会立即生效，但是为 Box 添加 Modifier.animatedContentSize 后，
                // 文本大小的变化会使用动画过渡
                .animateContentSize()
        ) {
            Text(
                text = "animateContentSize 是一个 Modifier 修饰符方法。它的用途非常专一，当容器尺寸发生变化时，会通过动画进行过渡，开箱即用。这个实例中，expend 决定文本的最大行数，也就决定了 Box 的整体尺寸，正常情况下大小的变化会立即生效，但是为 Box 添加 Modifier.animatedContentSize 后，文本大小的变化会使用动画过渡。",
                maxLines = if (expended) 100 else 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            expended = !expended
        }) {
            Text(text = "Click ME!")
        }
    }
}