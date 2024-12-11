package me.ztiany.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.compose.theme.UIJetpackComposeTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * check out <https://developer.android.com/develop/ui/compose/layouts/insets> for more information about
         * how to handle edge-to-edge display.
         */
        enableEdgeToEdge()
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