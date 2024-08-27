package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.modifier.ModifierLocalProvider
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.ProvidableModifierLocal
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import timber.log.Timber

/*
    ModifierLocal 于 ThreadLocal, CompositionLocal 中的 Local 类似。
    它让父子关系的多个 Modifier 之间可以共享数据。
 */
@Composable
fun S519_ModifierLocal() {
    val localKey = modifierLocalOf { mutableListOf("") }

    // Modifier.windowInsetsPadding() 就是使用了 ModifierLocal。
    Modifier.windowInsetsPadding(WindowInsets(10.dp))

    Box(Modifier
        .then(object : LayoutModifier, ModifierLocalProvider<MutableList<String>> {

            private var stringList = mutableListOf<String>()

            override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
                val placeable = measurable.measure(constraints)
                stringList.add("measure")
                return layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }

            override val key: ProvidableModifierLocal<MutableList<String>>
                get() = localKey

            // 提供数据
            override val value: MutableList<String>
                get() = stringList

        })
        .then(object : LayoutModifier, ModifierLocalConsumer {

            var stringList: MutableList<String> = mutableListOf()

            override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
                val placeable = measurable.measure(constraints)
                Timber.d("measure.stringList: $stringList")
                return layout(placeable.width, placeable.height) {
                    Timber.d("layout.stringList: $stringList")
                    placeable.placeRelative(0, 0)
                }
            }

            // 消费数据
            override fun onModifierLocalsUpdated(scope: ModifierLocalReadScope) = with(scope) {
                stringList = localKey.current
                Timber.d("modifierLocalConsumer.key.current: ${localKey.current}")
            }
        }
            .background(color = Color.Red)
            .size(100.dp))
    ) {

    }
}