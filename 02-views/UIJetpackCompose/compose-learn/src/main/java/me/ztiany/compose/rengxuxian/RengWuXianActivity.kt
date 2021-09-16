package me.ztiany.compose.rengxuxian

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ztiany.compose.learn.ui.theme.UIJetpackComposeTheme

class RengWuXianActivity : AppCompatActivity() {

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

                Lesson07(Modifier.size(40.dp))
            }
        }
    }


}