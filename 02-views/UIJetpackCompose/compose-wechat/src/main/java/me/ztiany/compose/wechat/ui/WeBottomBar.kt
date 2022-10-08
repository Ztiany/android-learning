package me.ztiany.compose.wechat.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ztiany.compose.wechat.R
import me.ztiany.compose.wechat.ui.theme.WeTheme

private const val TAG = "WeBottomBar"

@Composable
fun WeBottomBar(selected: Int, onBottomBarSelected: (Int) -> Unit) {

    Log.d(TAG, "WeTheme.colors.bottomBar = ${WeTheme.colors.bottomBar}")
    Log.d(TAG, "WeTheme.colors.iconCurrent = ${WeTheme.colors.iconCurrent}")
    Log.d(TAG, "WeTheme.colors.icon = ${WeTheme.colors.icon}")

    //ViewModel 会导致预览失败，因为预览不支持 ViewModel，因此可以将变更 selectedTab 的具体操作提取出去。
    // val viewModel: WeViewModel = viewModel()

    /*Row 一行，就是一个横向布局*/
    //CompositionLocal：只在某一段 Compose 范围起作用的变量。
    Row(Modifier.background(WeTheme.colors.bottomBar)) {

        TableItem(
            if (selected == 0) R.drawable.ic_chat_filled else R.drawable.ic_chat_outlined,
            "聊天",
            if (selected == 0) WeTheme.colors.iconCurrent else WeTheme.colors.icon,
            Modifier
                .clickable {
                    onBottomBarSelected(0)
                }
                .weight(1F)
        )

        TableItem(
            if (selected == 1) R.drawable.ic_contacts_filled else R.drawable.ic_contacts_outlined,
            "通讯录",
            if (selected == 1) WeTheme.colors.iconCurrent else WeTheme.colors.icon,
            Modifier
                .clickable {
                    onBottomBarSelected(1)
                }
                .weight(1F),
        )

        TableItem(
            if (selected == 2) R.drawable.ic_discover_filled else R.drawable.ic_discover_outlined,
            "发现",
            if (selected == 2) WeTheme.colors.iconCurrent else WeTheme.colors.icon,
            Modifier
                .clickable {
                    onBottomBarSelected(2)
                }
                .weight(1F)
        )

        TableItem(
            if (selected == 3) R.drawable.ic_me_filled else R.drawable.ic_me_outlined,
            "我",
            if (selected == 3) WeTheme.colors.iconCurrent else WeTheme.colors.icon,
            Modifier
                .clickable {
                    onBottomBarSelected(3)
                }
                .weight(1F)
        )
    }
}

@Composable
private fun TableItem(
    @DrawableRes iconId: Int,
    title: String,
    tint: Color,
    modifier: Modifier
) {
    Column(
        modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        /*
            1. Modifier 在 Compose 中的地位非常重要，很多功能都是通过 Modifier 实现的。
            2. Compose 里面只有 Padding，没有 Margin，但是使用 Padding 可以实现传统 View 的内外边距效果。
         */
        Icon(ImageVector.vectorResource(iconId), title, Modifier.size(24.dp), tint = tint)
        Text(title, fontSize = 11.sp, color = tint)
    }
}

@Preview
@Composable
fun WeBottomBarPreview1() {
    WeTheme(WeTheme.Theme.Light) {
        var selectedTab by remember { mutableStateOf(0) }
        WeBottomBar(selectedTab) {
            selectedTab = it
        }
    }
}

@Preview
@Composable
fun WeBottomBarPreview2() {
    WeTheme(WeTheme.Theme.Dark) {
        var selectedTab by remember { mutableStateOf(0) }
        WeBottomBar(selectedTab) {
            selectedTab = it
        }
    }
}

@Preview
@Composable
fun WeBottomBarPreview3() {
    WeTheme(WeTheme.Theme.NewYear) {
        var selectedTab by remember { mutableStateOf(0) }
        WeBottomBar(selectedTab) {
            selectedTab = it
        }
    }
}