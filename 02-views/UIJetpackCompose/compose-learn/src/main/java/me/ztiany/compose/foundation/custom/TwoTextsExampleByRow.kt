package me.ztiany.compose.foundation.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TwoTexts(
    text1: String = "Text1",
    text2: String = "Text2",
    modifier: Modifier = Modifier
) {
    /*
    height(IntrinsicSize.Min) 可将其子项的高度强行调整为最小固有高度。
            Row 可组合项的 minIntrinsicHeight 将作为其子项的最大 minIntrinsicHeight。
            Divider 元素的 minIntrinsicHeight 为 0，因为如果没有给出约束条件，它不会占用任何空间；
            如果给出特定 width，Text minIntrinsicHeight 将为文本的高度。因此，Row 元素的 height 约
            束条件将为 Text 的最大 minIntrinsicHeight；而 Divider 会将其 height 扩展为 Row 给定的 height 约束条件。
     */
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
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