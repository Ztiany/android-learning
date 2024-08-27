package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/*
    SemanticsModifier 用来提供 semantic tree（语义树）。语义树是一个树形结构，用来描述 UI 的结构和语义。

    语义是编译原理中的概念，在 Compose 中，语义就是“意思”的意思，语义树就是用来描述 UI 的意思的树。

    Compose 中会对组件树进行修剪，去掉或合并那些没有语义的节点，最终形成一个语义树。

    比如 Column 就是一个无语义的节点，它只提供布局，没有语义，所以在语义树中就会被去掉。

    SemanticsModifier 用于：
        - 无障碍
        - 开发测试

    这是主流操作系统里面都有的功能。
 */
@Composable
fun S513_SemanticsModifier() {
    Column {
        Text(text = "Hello")// 对于 Text，默认就支持无障碍。
        Box(modifier = Modifier
            .size(100.dp)
            .semantics(mergeDescendants = true/* mergeDescendants 表示需要合并语义树，则组件合并内部的语义 */) {
                contentDescription = "我是一个 Box"
            })// 对于 Box，内部没有语义，所以需要手动添加。
    }
}