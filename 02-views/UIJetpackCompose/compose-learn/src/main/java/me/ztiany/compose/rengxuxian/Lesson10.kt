package me.ztiany.compose.rengxuxian

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import me.ztiany.compose.R


fun Lesson10_interactWithTraditionalView(activity: AppCompatActivity) = with(activity) {
    val context = this

    setContentView(R.layout.activity_rengwuxian_lesson10)

    findViewById<ComposeView>(R.id.composeView).setContent {
        Column {
            Text(text = "Haha, I am Text created by Compose.")

            //添加传统的 View
            AndroidView({
                TextView(context).apply {
                    setBackgroundColor(Color.RED)
                    text = "Traditional View added to Compose."
                }
            }, Modifier.fillMaxWidth()) {
                //在 recompose 过程执行，用于更新。
            }

            Text(text = "Haha, I am Text created by Compose.")
        }
    }

}