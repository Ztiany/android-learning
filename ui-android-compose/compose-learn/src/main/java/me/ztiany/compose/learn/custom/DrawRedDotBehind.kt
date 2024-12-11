package me.ztiany.compose.learn.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R

/** 演示  DrawBehind 的使用*/
@Composable
fun DrawRedDotBehind(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp), modifier = Modifier
                .size(100.dp)
                .drawBehind {
                    //drawBehind 实际上就是先绘制拓展的内容，再绘制组件本身，也就是用来自定义绘制组件背景的。
                    drawCircle(
                        Color(0xffe7614e),
                        18.dp.toPx() / 2,
                        center = Offset(drawContext.size.width, 0f)
                    )
                }
        ) {
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "Diana")
        }
    }
}