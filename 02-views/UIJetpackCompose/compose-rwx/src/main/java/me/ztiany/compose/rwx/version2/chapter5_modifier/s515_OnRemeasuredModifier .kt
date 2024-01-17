package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.OnRemeasuredModifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/*
    OnRemeasuredModifier：
        1. 用于实现 onSizeChanged 操作符。
        2. onSizeChanged 相当于在 View 的 onMeasure() 里调用 super.onMeasure() 拿到测量结果。
        3. OnRemeasuredModifier 的 onRemeasured 方法，在 NodeCoordinator 中被调用。
 */
@Composable
fun S515_OnRemeasuredModifier() {
    Column {
        Text(text = "Hello", Modifier
            // size 改变了，就会触发 onSizeChanged
            .onSizeChanged {

            }
            .size(100.dp)
            // 如果需要每次测量完成都触发，则需要手动添加 OnRemeasuredModifier。
            // 这里当 size(120.dp) 完成测量时，就会触发 onRemeasured。
            .then(object : OnRemeasuredModifier {
                // 每次测量完成，就会触发 onRemeasured
                override fun onRemeasured(size: IntSize) {

                }
            }.size(120.dp))
        )
    }
}