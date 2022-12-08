package me.ztiany.compose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.commom.UIJetpackComposeTheme
import me.ztiany.compose.foundation.animation.AnimationActivity
import me.ztiany.compose.foundation.custom.CustomLayoutActivity
import me.ztiany.compose.foundation.gesture.GestureActivity
import me.ztiany.compose.foundation.layout.LayoutBasicActivity
import me.ztiany.compose.foundation.local.CompositionLocalActivity
import me.ztiany.compose.foundation.material.MaterialActivity
import me.ztiany.compose.foundation.sideeffect.SideEffectActivity
import me.ztiany.compose.foundation.tutor.TutorActivity

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
    private fun Entrances() {
        EnterButton(TutorActivity::class.java, "Compose 博物馆入门教程")
        EnterButton(LayoutBasicActivity::class.java, "Compose 基础组件学习")
        EnterButton(MaterialActivity::class.java, "Compose Material 组件学习")
        EnterButton(CompositionLocalActivity::class.java, "CompositionLocal 学习")
        EnterButton(SideEffectActivity::class.java, "Side Effect API 学习")
        EnterButton(CustomLayoutActivity::class.java, "自定义 Compose 渲染流程")
        EnterButton(AnimationActivity::class.java, "动画 API 学习")
        EnterButton(GestureActivity::class.java, "手势 API 学习")
    }

    @Composable
    private fun EnterButton(clazz: Class<out Activity>, name: String) {
        Button(onClick = {
            startActivity(Intent(this@MainActivity, clazz))
        }, Modifier.padding(5.dp).fillMaxWidth()) {
            Icon(
                painter = painterResource(id = android.R.drawable.star_on),
                contentDescription = name
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(name)
        }
    }

}