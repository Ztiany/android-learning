package me.ztiany.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.compose.theme.UIJetpackComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
          setContent 是一个扩展方法，内部会尝试找到一个 ComposeView，如果找不到就创建一个 ComposeView。
          然后基于这个 ComposeView 构建界面。
         */
        setContent {
            UIJetpackComposeTheme {
                AppNavGraph(startDestination = MAIN_SCREEN)
            }
        }
    }

}