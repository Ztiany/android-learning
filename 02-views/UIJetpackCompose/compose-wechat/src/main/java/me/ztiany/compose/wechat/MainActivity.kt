package me.ztiany.compose.wechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.ViewModelProvider
import me.ztiany.compose.wechat.ui.WeBottomBar
import me.ztiany.compose.wechat.ui.theme.WeTheme

class MainActivity : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(WeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeTheme(WeTheme.Theme.NewYear) {
                /*Column 一列，就是垂直布局的*/
                Column {
                    WeBottomBar(viewModel.selectedTab)
                }
            }
        }
    }

}
