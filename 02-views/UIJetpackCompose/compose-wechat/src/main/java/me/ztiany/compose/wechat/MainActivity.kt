package me.ztiany.compose.wechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ztiany.compose.wechat.ui.WeBottomBar
import me.ztiany.compose.wechat.ui.theme.WeTheme

/*
笔记:

    Ambient：
        1. 解释为 the temperature etc of the surrounding area 环境温度/光线等。
        2. 作用是用于设置一些通用的属性，比如背景色等。
 */
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
