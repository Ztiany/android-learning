package me.ztiany.compose.rwx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ContentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            intent.getStringExtra("name")?.let {
                InvokeComposable(it)
            }
        }
    }

}