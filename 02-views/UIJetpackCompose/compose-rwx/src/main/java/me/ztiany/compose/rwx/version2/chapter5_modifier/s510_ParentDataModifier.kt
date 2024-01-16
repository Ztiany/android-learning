package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
    ParentDataModifier 的作用：ParentDataModifier 在布局测量过程中起到辅助作用。
 */
@Composable
fun S510_ParentDataModifier() {
    /*
    比如，weight 就是借助了 ParentDataModifier 来实现的。
    ParentDataModifier 用于在测量过程中，让被测量和布局的子控件传递一些信息给父控件。
    比如，这里的 weight，就需要子控件来告诉父控件，单凭父控件的布局算法是无法实现这个效果的（或者说单纯靠父控件来实现这个效果，将会变得很复杂）。

    对于像这种需要子控件来告诉父控件一些信息的需求，Compose 团队就设计了 ParentDataModifier。类似的用到 ParentDataModifier 的还有：

        - layoutId
        - align
    */
    Row {
        Box(modifier = Modifier
            .size(40.dp)
            .background(Color.Blue)
            .weight(1F))
        Box(modifier = Modifier
            .size(40.dp)
            .background(Color.Red))
        Box(modifier = Modifier
            .size(40.dp)
            .background(Color.Green))
    }

}