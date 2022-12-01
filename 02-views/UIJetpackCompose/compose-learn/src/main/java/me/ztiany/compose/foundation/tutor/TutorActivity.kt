package me.ztiany.compose.foundation.tutor

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R
import me.ztiany.compose.commom.UIJetpackComposeTheme

data class Message(val author: String, val body: String)

private const val author = "Jetpack Compose 博物馆"

private val messages = listOf(
    Message(author, "我们开始更新啦"),
    Message(author, "为了给广大的读者一个更好的体验，从今天起，我们公众号决定陆续发一些其他作者的高质量文章"),
    Message(author, "每逢佳节倍思亲，从今天起，参加我们公众号活动的同学可以获得精美礼品一份！！"),
    Message(author, "荣华梦一场，功名纸半张，是非海波千丈，马蹄踏碎禁街霜，听几度头鸡唱"),
    Message(author, "唤归来，西湖山上野猿哀。二十年多少风流怪，花落花开。望云霄拜将台，袖星斗安邦策，破烟月迷魂寨。酸斋笑我，我笑酸斋"),
    Message(author, "伤心尽处露笑颜，醉里孤单写狂欢。两路殊途情何奈，三千弱水忧忘川。花开彼岸朦胧色，月过长空爽朗天。青鸟思飞无侧羽，重山万水亦徒然"),
    Message(author, "又到绿杨曾折处，不语垂鞭，踏遍清秋路。衰草连天无意绪，雁声远向萧关去。恨天涯行役苦，只恨西风，吹梦成今古。明日客程还几许，沾衣况是新寒雨"),
    Message(author, "莫笑农家腊酒浑，丰年留客足鸡豚。山重水复疑无路，柳暗花明又一村。箫鼓追随春社近，衣冠简朴古风存。从今若许闲乘月，拄杖无时夜叩门")
)

class TutorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                Conversation(messages)
            }
        }
    }

    @Composable
    fun Conversation(messages: List<Message>) {
        LazyColumn {
            items(messages) { message ->
                MessageCard(msg = message)
            }
        }
    }

    @Composable
    fun MessageCard(msg: Message) {
        Surface(
            shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
            elevation = 5.dp,
            modifier = Modifier.padding(all = 8.dp)
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
                Column {
                    Text(
                        text = msg.author,
                        color = MaterialTheme.colors.secondaryVariant,// 添加颜色
                        style = MaterialTheme.typography.subtitle2 // 添加 style
                    )
                    Spacer(Modifier.padding(vertical = 4.dp))
                    Text(
                        text = msg.body,
                        style = MaterialTheme.typography.body2 // 添加 style
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
            Conversation(messages)
        }
    }

}