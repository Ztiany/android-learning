package me.ztiany.compose.wechat.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import me.ztiany.compose.wechat.R
import me.ztiany.compose.wechat.WeTopBar
import me.ztiany.compose.wechat.WeViewModel
import me.ztiany.compose.wechat.data.Msg
import me.ztiany.compose.wechat.data.User
import me.ztiany.compose.wechat.ui.theme.WeTheme
import kotlin.math.roundToInt

@Composable
fun ChatPage() {
    val viewModel: WeViewModel = viewModel()
    //动画化 offset
    val offsetPercentX by animateFloatAsState(if (viewModel.chatting) 0F else 1F)
    //当前聊天
    val chat = viewModel.currentChat ?: return

    Column(
        modifier = Modifier
            //不知都要偏移多少，不用  offset
            //.offset()
            //使用自定义布局，可与知道自己要偏移多少
            .offsetPercent(offsetPercentX = offsetPercentX)
            .background(WeTheme.colors.background)
            .fillMaxSize()
    ) {

        //炸弹动画变量
        var shakingTime by remember {
            mutableStateOf(0)
        }

        //标题栏
        WeTopBar(chat.friend.name) {
            viewModel.endChat()
        }

        //中间的内容
        Box(
            Modifier
                .background(WeTheme.colors.chatPage)
                .weight(1F)
        ) {

            //特效
            Box(
                Modifier
                    .alpha(WeTheme.colors.chatPageBgAlpha)
                    .fillMaxSize()
            ) {
                Image(
                    painterResource(R.drawable.ic_bg_newyear_left),
                    null,
                    Modifier
                        .align(Alignment.CenterStart)
                        .padding(bottom = 100.dp)
                )
                Image(
                    painterResource(R.drawable.ic_bg_newyear_top),
                    null,
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 24.dp)
                )
                Image(
                    painterResource(R.drawable.ic_bg_newyear_right),
                    null,
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(vertical = 200.dp)
                )
            }

            //炸弹动画变量
            val shakingOffset = remember {
                Animatable(0F)
            }
            LaunchedEffect(key1 = shakingTime) {
                if (shakingTime != 0) {//防止一进来就有炸弹动画
                    shakingOffset.animateTo(
                        0F, animationSpec = spring(0.3F, 600F), initialVelocity = -2000F
                    )
                }
            }

            //聊天记录
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .offset(shakingOffset.value.dp, shakingOffset.value.dp)
            ) {
                items(chat.msgs.size) { index ->
                    val msg = chat.msgs[index]
                    MessageItem(msg, shakingTime, chat.msgs.size - index - 1)
                }
            }
        }

        // 底部
        ChatBottomBar(onBombClicked = {
            viewModel.bomb(chat)
            //触发动画
            shakingTime++
        })
    }
}

@Composable
fun MessageItem(msg: Msg, shakingTime: Int, shakingLevel: Int) {
    val shakingAngleBubble = remember { Animatable(0f) }
    LaunchedEffect(key1 = shakingTime, block = {
        if (shakingTime != 0) {
            delay(shakingLevel.toLong() * 30)
            shakingAngleBubble.animateTo(
                0f,
                animationSpec = spring(0.4f, 500f),
                initialVelocity = 1200f / (1 + shakingLevel * 0.4f)
            )
        }
    })

    if (msg.from == User.Me) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.End
        ) {
            val bubbleColor = WeTheme.colors.bubbleMe
            Text(msg.text,
                Modifier
                    .graphicsLayer(
                        rotationZ = shakingAngleBubble.value,
                        transformOrigin = TransformOrigin(1f, 0f)
                    )
                    .drawBehind {
                        val bubble = Path().apply {
                            val rect = RoundRect(
                                10.dp.toPx(),
                                0f,
                                size.width - 10.dp.toPx(),
                                size.height,
                                4.dp.toPx(),
                                4.dp.toPx()
                            )
                            addRoundRect(rect)
                            moveTo(size.width - 10.dp.toPx(), 15.dp.toPx())
                            lineTo(size.width - 5.dp.toPx(), 20.dp.toPx())
                            lineTo(size.width - 10.dp.toPx(), 25.dp.toPx())
                            close()
                        }
                        drawPath(bubble, bubbleColor)
                    }
                    .padding(20.dp, 10.dp), color = WeTheme.colors.textPrimaryMe)
            Image(
                painterResource(msg.from.avatar),
                contentDescription = msg.from.name,
                Modifier
                    .graphicsLayer(
                        rotationZ = shakingAngleBubble.value * 0.6f,
                        transformOrigin = TransformOrigin(1f, 0f)
                    )
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    } else {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painterResource(msg.from.avatar),
                contentDescription = msg.from.name,
                Modifier
                    .graphicsLayer(
                        rotationZ = -shakingAngleBubble.value * 0.6f,
                        transformOrigin = TransformOrigin(0f, 0f)
                    )
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            val bubbleColor = WeTheme.colors.bubbleOthers
            Text(msg.text,
                Modifier
                    .graphicsLayer(
                        rotationZ = -shakingAngleBubble.value,
                        transformOrigin = TransformOrigin(0f, 0f)
                    )
                    .drawBehind {
                        val bubble = Path().apply {
                            val rect = RoundRect(
                                10.dp.toPx(),
                                0f,
                                size.width - 10.dp.toPx(),
                                size.height,
                                4.dp.toPx(),
                                4.dp.toPx()
                            )
                            addRoundRect(rect)
                            moveTo(10.dp.toPx(), 15.dp.toPx())
                            lineTo(5.dp.toPx(), 20.dp.toPx())
                            lineTo(10.dp.toPx(), 25.dp.toPx())
                            close()
                        }
                        drawPath(bubble, bubbleColor)
                    }
                    .padding(20.dp, 10.dp), color = WeTheme.colors.textPrimary)
        }
    }
}

@Composable
fun ChatBottomBar(onBombClicked: () -> Unit) {
    var editingText by remember { mutableStateOf("") }
    Row(
        Modifier
            .fillMaxWidth()
            .background(WeTheme.colors.bottomBar)
            .padding(4.dp, 0.dp)
    ) {
        Icon(
            painterResource(R.drawable.ic_voice),
            contentDescription = null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp)
                .size(28.dp),
            tint = WeTheme.colors.icon
        )
        BasicTextField(
            editingText,
            { editingText = it },
            Modifier
                .weight(1f)
                .padding(4.dp, 8.dp)
                .height(40.dp)
                .clip(MaterialTheme.shapes.small)
                .background(WeTheme.colors.textFieldBackground)
                .padding(start = 8.dp, top = 10.dp, end = 8.dp),
            cursorBrush = SolidColor(WeTheme.colors.textPrimary)
        )
        Text(
            "\uD83D\uDCA3",
            Modifier
                .clickable(onClick = onBombClicked)
                .padding(4.dp)
                .align(Alignment.CenterVertically),
            fontSize = 24.sp
        )
        Icon(
            painterResource(R.drawable.ic_add),
            contentDescription = null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp)
                .size(28.dp),
            tint = WeTheme.colors.icon
        )
    }
}

fun Modifier.offsetPercent(offsetPercentX: Float = 0F, offsetPercentY: Float = 0F) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val offsetX = (offsetPercentX * placeable.width).roundToInt()
        val offsetY = (offsetPercentY * placeable.width).roundToInt()
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(offsetX, offsetY)
        }
    }
