package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.dp

@Composable
fun S506_DrawModifier() {
    Column {
        Demo1()
        Demo2()
    }
}

@Composable
private fun Demo2() {
    // 左边是外部，右边是内部。证明：左边的 drawWithContent 会覆盖右边的 background 的效果。
    // 因此，DrawModifier 是[替换]绘制内容，而不是[增加]绘制内容。这就是为什么需要在 drawWithContent 里面调用 drawContent() 的原因。
    Column {
        // 正常
        Box(modifier = Modifier
            .background(Color.Red)
            .size(40.dp)
            .drawWithContent {

            }) {
        }

        Box(modifier = Modifier
            .size(40.dp)
            // drawWithContent 会覆盖 background 的效果
            .drawWithContent {
                //drawContent()，这里的 drawContent() 会绘制所有后续的 DrawModifier 所绘制的内容。
            }
            // 无效果
            .background(Color.Red)) {
        }
    }
}

@Composable
private fun Demo1() {
    Modifier.then(object : DrawModifier {
        override fun ContentDrawScope.draw() {

        }
    })

    // 等价于

    Modifier.drawWithContent {

    }
}