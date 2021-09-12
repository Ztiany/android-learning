package me.ztiany.compose.learn

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ztiany.compose.R
import me.ztiany.compose.learn.ui.theme.UIJetpackComposeTheme
import me.ztiany.compose.rengxuxian.RengWuXianActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
          setContent 是一个扩展方法，内部会尝试找到一个 ComposeView，如果找不到就创建一个 ComposeView。
            然后基于这个 ComposeView 构建界面。
         */
        setContent {
            UIJetpackComposeTheme {
                /*
                  A surface container using the 'background' color from the theme
                    1. Surface 是 MD 里面的一个概念。
                    2. Surface 具有一些特性，比如背景是黑色，里面的文字就会自动变为白色。
                    3. Surface 不是必须的。
                 */
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier
                            .padding(0.dp, 20.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, RengWuXianActivity::class.java))
                        }) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.star_on),
                                contentDescription = ""
                            )
                            Text("扔物线 Compose 课程")
                        }
                    }
                }
            }
        }

    }
}