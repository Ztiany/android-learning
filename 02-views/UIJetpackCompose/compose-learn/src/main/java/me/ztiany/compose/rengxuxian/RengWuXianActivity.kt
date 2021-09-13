package me.ztiany.compose.rengxuxian

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import me.ztiany.compose.learn.ui.theme.UIJetpackComposeTheme

class RengWuXianActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                Text(text = "")
                //Lesson01_Basic()
                Lesson01_List()
            }
        }
    }

}