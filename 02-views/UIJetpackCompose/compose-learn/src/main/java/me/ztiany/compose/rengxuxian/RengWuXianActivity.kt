package me.ztiany.compose.rengxuxian

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ztiany.compose.learn.ui.theme.UIJetpackComposeTheme

class RengWuXianActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                Text(text = "")
                //Lesson02_Basic()
                //Lesson02_List()
                //Lesson04_CustomComposable()
                //Lesson05_DynamicUI(createDynamicName(lifecycleScope))
                //Lesson05_DynamicUI_Bad(lifecycleScope)
                //Lesson05_DynamicUI_Good(lifecycleScope)
                //Lesson06_State()
                Lesson06_Recompose_AutoOptimize()
            }
        }
    }


}