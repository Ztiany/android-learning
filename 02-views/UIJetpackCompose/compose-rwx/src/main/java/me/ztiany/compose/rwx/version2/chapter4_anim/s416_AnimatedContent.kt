package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
    AnimatedVisibility 可以对单个组件的出场入场进行动画，且可以进行精细的控制。
    Crossfade 可以对多个组件的切换进行动画，但是只能做淡入淡出的动画。

    而 AnimatedContent 可以对多个组件的切换进行动画，且可以进行精细的控制。
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun S416_AnimatedContent() {
    Column {
        Content1()
        Content2(false)
        Content2(true)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Content2(clip: Boolean) {
    var shown by remember {
        mutableStateOf(true)
    }

    // 当两个组件的大小不同时，AnimatedContent 会自动用动画来实际大小的变化。使用 SizeTransform 用来指定放大和缩小的过程中大的那个组件要不要被裁剪至小的那个组件的大小。
    // 一般情况下是应该设置为裁剪的，这样才有组件从小到大的动画效果。
    Column {
        AnimatedContent(
            shown,
            label = "S416_AnimatedContent_2",
            transitionSpec = {
                (fadeIn(animationSpec = tween(2200, delayMillis = 190)) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(2200, delayMillis = 90)
                ) with fadeOut(animationSpec = tween(900))).using(SizeTransform(clip = clip))
            },

            ) {
            if (it) {
                Box1()
            } else {
                Box2()
            }
        }
        Button(onClick = {
            shown = !shown
        }) {
            Text(text = "切换")
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun Content1() {
    var shown by remember {
        mutableStateOf(true)
    }

    Column {
        AnimatedContent(
            shown,
            label = "S416_AnimatedContent_1",
            transitionSpec = {
                if (targetState) {
                    (fadeIn(tween(durationMillis = 3000)) with fadeOut(tween(durationMillis = 3000, delayMillis = 1100))).apply {
                        // 控制谁被覆盖，这种配置下，绿色的 Box2 始终在红色的 Box1 上面。
                        targetContentZIndex = -1F
                    }
                } else {
                    (fadeIn(tween(durationMillis = 3000)) with fadeOut(tween(durationMillis = 3000, delayMillis = 1100)))
                }
            },

            ) {
            if (it) {
                Box1()
            } else {
                Box2()
            }
        }
        Button(onClick = {
            shown = !shown
        }) {
            Text(text = "切换")
        }
    }
}


@Composable
private fun Box1() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Red)
    )
}

@Composable
private fun Box2() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(Color.Green)
    )
}
