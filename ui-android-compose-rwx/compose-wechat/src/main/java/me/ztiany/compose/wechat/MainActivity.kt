package me.ztiany.compose.wechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.lifecycle.ViewModelProvider
import me.ztiany.compose.wechat.ui.ChatPage
import me.ztiany.compose.wechat.ui.Home
import me.ztiany.compose.wechat.ui.theme.WeTheme

class MainActivity : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[WeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeTheme(viewModel.theme) {
                Box {
                    Home(viewModel = viewModel)
                    ChatPage()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!viewModel.endChat()) {
            super.onBackPressed()
        }
    }

}
