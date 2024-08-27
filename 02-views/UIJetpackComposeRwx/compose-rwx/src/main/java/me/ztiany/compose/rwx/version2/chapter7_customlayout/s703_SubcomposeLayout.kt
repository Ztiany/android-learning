package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout

@Composable
fun S703_SubcomposeLayout() {
    Demo1()
    Demo2()
}

@Composable
private fun Demo2() {
    // SubcomposeLayout 把组合过程推迟到了 layout 时，这样就可以根据布局的大小来决定组合的内容。
    // 但是 SubcomposeLayout 自己维护了一个 slot table，无法参与到整体的性能优化中。
    SubcomposeLayout {
        val measurable = subcompose(1) {
            Text(text = "Hello")
        }[0]
        val placeable = measurable.measure(it)

        // 可以根据第一个组合的大小来决定第二个组合的内容。
        if (placeable.width > 100) {
            val measurable2 = subcompose(2) {
                Text(text = "World")
            }[0]
        }

        layout(placeable.width, placeable.height) {

        }
    }
}

@Composable
private fun Demo1() {
    // Scaffold, BoxWithConstraints, LazyColumn 就是使用的 SubcomposeLayout
    BoxWithConstraints {

    }
}