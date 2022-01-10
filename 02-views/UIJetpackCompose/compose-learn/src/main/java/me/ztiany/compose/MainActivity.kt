package me.ztiany.compose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.commom.UIJetpackComposeTheme
import me.ztiany.compose.foundation.layout.LayoutBasicActivity
import me.ztiany.compose.foundation.local.CompositionLocalActivity
import me.ztiany.compose.foundation.sideeffect.SideEffectActivity
import me.ztiany.compose.rengxuxian.RengWuXianComposeActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
          setContent 是一个扩展方法，内部会尝试找到一个 ComposeView，如果找不到就创建一个 ComposeView。
          然后基于这个 ComposeView 构建界面。
         */
        setContent {
            MainContent()
        }
    }

    @Composable
    private fun Entrances() {
        EnterButton(LayoutBasicActivity::class.java, "Compose 布局基础基础")
        EnterButton(SideEffectActivity::class.java, "Compose Side Effect 演示")
        EnterButton(CompositionLocalActivity::class.java, "CompositionLocal 演示")
        EnterButton(RengWuXianComposeActivity::class.java, "扔物线 Compose 课程")
    }

    @Composable
    private fun MainContent() {
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
                    Entrances()
                }
            }
        }
    }

    @Composable
    private fun EnterButton(clazz: Class<out Activity>, name: String) {
        Button(onClick = {
            startActivity(Intent(this@MainActivity, clazz))
        }, Modifier.padding(5.dp)) {
            Icon(
                painter = painterResource(id = android.R.drawable.star_on),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(name)
        }
    }

}