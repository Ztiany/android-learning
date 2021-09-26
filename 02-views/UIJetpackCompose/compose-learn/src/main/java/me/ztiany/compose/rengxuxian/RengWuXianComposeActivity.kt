package me.ztiany.compose.rengxuxian

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.compose.foundation.ui.theme.UIJetpackComposeTheme

class RengWuXianComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                //初步上手
                //Lesson02_Basic()
                //Lesson02_List()

                //自定义 Composable
                //Lesson04_CustomComposable()

                //状态订阅于自动更新
                //Lesson05_DynamicUI(createDynamicName(lifecycleScope))
                //Lesson05_DynamicUI_Bad(lifecycleScope)
                //Lesson05_DynamicUI_Good(lifecycleScope)

                //状态机制的背后
                //Lesson06_State()
                //Lesson06_Recompose_AutoOptimize()

                //Modifier 深度解析
                //Lesson07()

                //动画
                //Lesson08()

                //自定义 View 的等价物
                Lesson09()
            }
        }
    }

}