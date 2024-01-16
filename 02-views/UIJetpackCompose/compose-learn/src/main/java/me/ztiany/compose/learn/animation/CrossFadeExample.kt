package me.ztiany.compose.learn.animation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

/** 演示  CrossFade 的使用*/
@Composable
fun CrossFadePage() {
    var currentPage by remember {
        mutableStateOf("A")
    }
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            /*
            Crossfade 可以理解为 AnimatedContent 的一种功能特性，它使用起来更简单，如果只需要淡入淡出效果，可以使用 Crossfade 替代 AnimatedContent。

            下面 Crossfade 内的文本会以淡入淡出的形式进行切换。

            其实更正确的说法应该是 AnimatedContent 是 Crossfade 的一种泛化，Crossfade 的API出现后，为了强化切换动画的能力，增加了AnimatedContent。

            需要注意Crossfade无法实现SizeTransform那样尺寸变化的动画效果，如果content变化前后尺寸不同，想使用动画进行过渡，可以使用AnimatedContent+SizeTranform的组合方案，或者使用Crossfade和接下来要介绍的Modifier.animateContentSize。
             */
            Crossfade(targetState = currentPage) {
                when (it) {
                    "A" -> Text(text = "A", modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray), textAlign = TextAlign.Center)
                    "B" -> Text(text = "B", modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Magenta), textAlign = TextAlign.Center)
                }
            }
        }
        Row(
            Modifier
                .background(Color.Red)
                .fillMaxWidth()
        ) {
            Button(onClick = { currentPage = "A" }, Modifier.weight(1F)) {
                Text(text = "A", Modifier.fillMaxWidth())
            }
            Button(onClick = { currentPage = "B" }, Modifier.weight(1F)) {
                Text(text = "B", Modifier.fillMaxWidth())
            }
        }
    }
}