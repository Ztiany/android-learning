package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun S411_ObserveFrame() {
    // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
    val scope = rememberCoroutineScope()

    val paddingAnimatable = remember {
        Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
    }

    val paddingValue = remember {
        mutableStateOf(paddingAnimatable.value)
    }

    Row {
        Box(
            Modifier
                .padding(0.dp, paddingAnimatable.value, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Yellow)
                .clickable {
                    scope.launch {
                        // 监听每一帧的一个使用场景是，让一个组件伴随领一个组件的动画而动画。
                        paddingAnimatable.animateDecay(1000.dp, exponentialDecay()) {
                            paddingValue.value = value
                        }
                    }
                })
        Box(
            Modifier
                .padding(0.dp, paddingValue.value, 0.dp, 0.dp)
                .size(100.dp)
                .background(Color.Green)
        )
    }

}