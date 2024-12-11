package me.ztiany.compose.wechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ztiany.compose.wechat.WeTopBar
import me.ztiany.compose.wechat.WeViewModel
import me.ztiany.compose.wechat.data.Chat
import me.ztiany.compose.wechat.ui.theme.WeTheme

@Composable
fun ChatList(chats: List<Chat>) {
    Column(
        Modifier
            .background(WeTheme.colors.background)
            .fillMaxSize()
    ) {
        //标题栏
        WeTopBar(title = "微信")
        //内容
        LazyColumn(Modifier.background(WeTheme.colors.listItem)) {
            itemsIndexed(chats) { index, chat ->
                ChatListItem(chat = chat)
                if (index < chats.lastIndex) {
                    Divider(
                        startIndent = 68.dp,
                        color = WeTheme.colors.chatListDivider,
                        thickness = 0.8f.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ChatListItem(chat: Chat) {
    val viewModel: WeViewModel = viewModel()

    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.startChat(chat)
            }) {
        //头像
        Image(
            painterResource(chat.friend.avatar),
            contentDescription = chat.friend.name,
            Modifier
                .padding(8.dp)
                .size(48.dp)
                .unread(!chat.msgs.last().read, WeTheme.colors.badge)
                .clip(RoundedCornerShape(4.dp))// shape 还可以切背景
        )
        //最后一条聊天
        Column(
            Modifier
                .weight(1F)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                chat.friend.name, fontSize = 17.sp, color = WeTheme.colors.textPrimary
            )
            Text(
                chat.msgs.last().text, fontSize = 14.sp, color = WeTheme.colors.textSecondary
            )
        }
        //时间
        Text(
            chat.msgs.last().time,
            Modifier.padding(8.dp, 8.dp, 12.dp, 8.dp),
            fontSize = 11.sp,
            color = WeTheme.colors.textSecondary
        )
    }
}

fun Modifier.unread(
    show: Boolean, color: Color
): Modifier = this.drawWithContent {
    //先画内容
    drawContent()
    if (show) {
        //再自己画
        drawCircle(
            color, 5.dp.toPx(), Offset(size.width - 1.dp.toPx(), 1.dp.toPx())
        )
    }
}