package me.ztiany.compose.foundation.tutor

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R
import me.ztiany.compose.theme.UIJetpackComposeTheme

@Composable
fun TutorPage() {
    ConversationList(messages)
}

@Composable
private fun ConversationList(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(msg = message)
        }
    }
}

@Composable
private fun MessageCard(msg: Message) {
    var isExpanded by remember { mutableStateOf(false) }
    val textBgColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )

    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp)
        ) {

            Image(
                painterResource(id = R.drawable.profile_picture),
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, shape = CircleShape) // 添加边框
            )

            Spacer(Modifier.padding(horizontal = 8.dp))

            Column(
                Modifier
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .animateContentSize()) {

                Text(
                    text = msg.author,
                    color = MaterialTheme.colors.secondaryVariant,// 添加颜色
                    style = MaterialTheme.typography.subtitle2, // 添加 style
                )

                Spacer(Modifier.padding(vertical = 4.dp))

                Text(
                    text = msg.body,
                    modifier = Modifier.background(textBgColor),
                    color = if (isExpanded) Color.White else Color.Blue,
                    style = MaterialTheme.typography.body2, // 添加 style
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    UIJetpackComposeTheme {
        ConversationList(messages)
    }
}