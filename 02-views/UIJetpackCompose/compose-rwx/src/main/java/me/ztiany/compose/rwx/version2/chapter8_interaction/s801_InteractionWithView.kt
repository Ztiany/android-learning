package me.ztiany.compose.rwx.version2.chapter8_interaction

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun S801_InteractionWithView() {
    /*
    在 Compose 中，没有对等的 SurfaceView 和 TextureView 实现，需要这样的功能，就必须用 AndroidView 来实现。

    因此 100% 使用 Compose 实现应用的 UI 是不可能的，但是可以使用 Compose 来实现应用的大部分 UI，然后使用 AndroidView 来实现一些特殊的 UI。
     */
    AndroidViewInCompose()
    ComposeInAndroidView(LocalContext.current)
}

private fun ComposeInAndroidView(context: Context) {
    LinearLayout(context).apply {
        addView(ComposeView(context).apply {

        })
    }
}

@Composable
private fun AndroidViewInCompose() {
    Column {
        AndroidView(factory = {
            TextView(it).apply {
                text = "AndroidView"
            }
        })
    }
}