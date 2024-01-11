package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
    SnapSpec 其实就是不做动画，立即变换到目标值，其实就是 sizeAnimatable.snapTo() 的意思，不过它可以设置延迟。
 */
@Composable
fun S404_AnimationSpec_SnapSpec() {
    var big by remember {
        mutableStateOf(false)
    }

    val size = remember(big) {
        if (big) {
            126.dp
        } else {
            58.dp
        }
    }

    val sizeAnimatable = remember {
        Animatable(
            initialValue = size, typeConverter = Dp.VectorConverter
        )
    }

    LaunchedEffect(big) {
        sizeAnimatable.animateTo(size, animationSpec = SnapSpec(delay = 1000))
    }

    Box(
        Modifier
            .size(sizeAnimatable.value)
            .background(Color.Red)
            .clickable {
                big = !big
            })
}