package me.ztiany.compose.rwx.version1

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/*
 * Composable 注解：
 *      1. Composable 是一个标识符，用于告诉编译器插件这个方法用于生成界面，编译器插件会对这些方法做一些修改。
 *      2. 加了 Composable 的方法必须在别的加了 Composable 方法中调用。
 *      3. 不用于构建界面的函数就没必要加 Composable 注解了，没有作用，反而增加编译器的负担。
 *
 * 自定义 Composable 相当于什么？
 *      相当于 xml 布局文件 + 自定义 View/ViewGroup。既可以有布局又可以有逻辑，即具有 xml 简单直观的特点，又可以加上逻辑。
 * 自定义 Composable 的应用场景？
 *      1. 简单的布局拆分。【相当于 xml】
 *      2. 逻辑逻辑定制的界面。【相当于自定义 View/ViewGroup】
 *      3. 需要定制绘制、布局、触摸反馈的界面。【基于 Modifier】
 */
@Composable
fun Lesson04_CustomComposable(name: String = "") {
    val realName = remember(name) {
        if (name.isEmpty()) "没有名字" else name
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "你好", Modifier.padding(10.dp))
        Image(painter = painterResource(id = R.drawable.star_on), contentDescription = "")
        Text(text = realName, Modifier.padding(10.dp))
    }
}