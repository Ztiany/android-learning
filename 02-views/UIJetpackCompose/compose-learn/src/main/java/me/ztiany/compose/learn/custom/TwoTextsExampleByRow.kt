package me.ztiany.compose.learn.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 演示 IntrinsicSize 的使用【实现 TwoTextsWithDivider.png 中展示的效果】*/
@Composable
fun TwoTextsExampleByRow(
    modifier: Modifier = Modifier,
    text1: String = "Text1",
    text2: String = "Text2"
) {
    /*
    height(IntrinsicSize.Min) 可将其子项的高度强行调整为最小固有高度。

                （1）下面仅使用 Modifier.height(IntrinsicSize. Min) 为高度设置了固有特性测量，宽度并没有进行设置。
                此时就表示当宽度不限定时，根据子组件预先测量的宽高信息所能计算的当前组件的高度来设置，最小可以是多少就设置多少。

                （2）我们只能对已经适配固有特性测量的内置组件使用 IntrinsicSize. Min 或 IntrinsicSize. Max，否则程序运行时会 crash。
     */
    Row(modifier = modifier.height(IntrinsicSize.Min)/*如果没有 IntrinsicSize.Min，则会占据整个高度空间*/) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}