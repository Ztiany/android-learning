package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
    AnimatedVisibility 是一个组件，它可以根据可见性来控制子组件的显示与隐藏。它是根据 Transition 来实现的。

    AnimatedVisibility 可用于出场入场的动画。

    有很多组件还扩展定制了 AnimatedVisibility，比如 ColumnScope.AnimatedVisibility。

    AnimatedVisibility 内部只能由一个子组件。
 */
@Composable
fun S414_AnimatedVisibility() {
    Row {
        Box1()
        Box2()
    }
}

@Composable
private fun Box1() {
    var big by remember {
        mutableStateOf(false)
    }

    /*
    AnimatedVisibility 的核心在于它的 enter: EnterTransition 和 exit: ExitTransition 参数。
        Transition 有四种，定义在 TransitionData 类中：
            internal data class TransitionData(
                val fade: Fade? = null,
                val slide: Slide? = null,
                val changeSize: ChangeSize? = null,
                val scale: Scale? = null
            )
        通过 Compose 提供的各种函数，可以创建这各种 Transition，各种 Transition 可以组合使用。

        常用的函数：
            fadeIn
            fadeOut

            slideIn
            slideOut

            scaleIn 默认只做缩放效果，实际的 size 不会变化。通过配置 transformOrigin 可以改变缩放的中心点。
            scaleOut

            expandIn 有一个裁切的效果，它是一点一点出现的。不过需要配置 initialSize 才有裁切的效果。
                expandVertically
                expandHorizontally
            shrinkOut
                shrinkVertically
                shrinkHorizontally

        注意 TransitionData 的 plus 操作符，不是完全的合并，而是相同的类型二选一。
     */

    Column {
        AnimatedVisibility(
            big,
            enter = fadeIn(tween(durationMillis = 2000), initialAlpha = 0.3F) + expandVertically()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Yellow)
            )
        }
        Button(onClick = {
            big = !big
        }) {
            Text(text = if (big) "Hide" else "Show")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Box2() {
    var big by remember { mutableStateOf(false) }
    val bigTransition = updateTransition(big, label = "big"/*label 参数用于预览界面的调试*/)

    Column {
        bigTransition.AnimatedVisibility(visible = { it }) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Yellow)
            )
        }
        Button(onClick = {
            big = !big
        }) {
            Text(text = if (big) "Hide" else "Show")
        }
    }

}