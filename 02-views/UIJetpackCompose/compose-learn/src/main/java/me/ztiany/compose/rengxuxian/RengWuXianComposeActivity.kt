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
                //Lesson02_Basic()
                //Lesson02_List()

                //Lesson04_CustomComposable()

                //Lesson05_DynamicUI(createDynamicName(lifecycleScope))
                //Lesson05_DynamicUI_Bad(lifecycleScope)
                //Lesson05_DynamicUI_Good(lifecycleScope)

                //Lesson06_State()
                //Lesson06_Recompose_AutoOptimize()

                Lesson07()
            }
        }
    }

}