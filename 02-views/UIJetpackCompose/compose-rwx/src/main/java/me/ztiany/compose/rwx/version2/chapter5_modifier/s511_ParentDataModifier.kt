package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import timber.log.Timber

/*
    ParentDataModifier 的写法：
        1. 只有在自定义布局的时候才会用到 ParentDataModifier。
        2. 在自定义布局的时候，需要在测量过程中，可以从 measurable 中获取到子控件的 data。
        3. 自定义一个 Modifier，这个 Modifier 内部使用 ParentDataModifier 来设置数据。
        4. 为了防止 API 污染，应该使用 LayoutScopeMarker 来标记这个 Modifier。
 */
@Composable
fun S511_ParentDataModifier() {
    CustomLayout {
        Text(
            text = "Hello", modifier = Modifier
                .weightData("Hello")
                .weightData("World")
        )
        Box {
            Text(
                text = "Hello", modifier = Modifier.background(Color.Blue)
                //.weightData("Hello") // 这里就不能调用了
            )
        }
    }
}


@LayoutScopeMarker
@Immutable
private interface CustomScope {

    @Stable
    fun Modifier.weightData(data: String): Modifier

}

@Composable
private fun CustomLayout(content: @Composable CustomScope.() -> Unit) {
    Layout(content = {
        CustomScopeInstance.content()
    }, measurePolicy = { measurables, constraints ->
        measurables.forEach {
            val data = it.parentData
        }

        // 测量逻辑...

        layout(0, 0) {
            // 布局逻辑...
        }
    })
}

private object CustomScopeInstance : CustomScope {

    override fun Modifier.weightData(data: String) = this.then(
        object : ParentDataModifier {
            // 如果同时调用了多此 weightData，那么 parentData 当前操作符右边的数据。
            override fun Density.modifyParentData(parentData: Any?): Any? {
                Timber.d("parentData: $parentData")
                Timber.d("data: $data")
                // 如果需要融合多个 ParentData 数据，那么可以提供一个数据结构来封装多个数据。即 data + parentData。
                // 具体可以参考 Compose 里面的相关实现。
                return data
            }
        }
    )

}