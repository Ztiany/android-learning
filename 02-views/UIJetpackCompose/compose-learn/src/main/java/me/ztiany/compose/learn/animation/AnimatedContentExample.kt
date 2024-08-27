package me.ztiany.compose.learn.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/** 演示 AnimatedContent 的使用 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentText() {
    /*
    AnimatedContent 和 AnimatedVisibility 相类似，都是用来为 content 添加动画效果的 Composable。区别在于 AnimatedVisibility 用来添加
    组件的出场与离场过渡动画，而 AnimatedContent 则是用来实现不同组件间的切换动画。

    AnimatedContent 参数上接收一个 targetState 和一个 content, content 是基于 targetState 创建的 Composable。当 targetState 变化时，
    content 的内容也会随之变化。AnimatedContent 内部维护着 targetState 到 conent 的映射表，查找 targetState 新旧值对应的 content 后，
    在 content 发生重组时附加动画效果。
     */
    Column {
        var count by remember { mutableStateOf(0) }

        /*
        下面代码单击按钮触发 count 发生变化，AnimatedContent 中 Text 的重组会应用动画效果。需要注意的是 targetState 一定要在 content
        中被使用，否则当 targetState 变化时，只见动画，却不见内容的变化，视觉上会很奇怪。
         */
        AnimatedContent(targetState = count) { targetCount ->
            // Make sure to use `targetCount`, not `count`.
            Text(text = "Count: $targetCount")
        }

        Button(onClick = { count++ }) {
            Text("Add")
        }
    }
}

/** 演示 AnimatedContent + ContentTransform 的使用 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentTextContentTransform() {
    var count by remember { mutableStateOf(0) }
    Column {

        /*
        AnimatedContent 默认动画是淡入淡出效果，还可以将 transitionSpec 参数指定为一个 ContentTransform 来自定义动画效果。
        ContentTransform 也是由 EnterTransition 与 ExitTransition 组合的，可以使用 with 中缀运算符将 EnterTransition 与 ExitTransition 组合起来。

        其实 ContentTransform 本质上就是 currentContent 的 ExitTransition 与 targetContent 的 EnterTransition 组合。

        例如使用 ContentTransform 实现一个 Slide 效果的切换动画：

            从右到左切换，并伴随淡入淡出效果：
                - EnterTransition：使用 slideInHorizontally，初始位置 initialOffsetX=width
                - ExitTransition：使用slideOutHorizontally，目标位置 targetOffsetX=-width

            从左到右切换，并伴随淡入淡出效果：
                - EnterTransition：使用 slideInHorizontally，初始位置 initialOffsetX=-width
                - ExitTransition：使用 slideOutHorizontally，目标位置 targetOffsetX=width
         */
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInVertically { height -> height } + fadeIn() with (slideOutVertically { height -> -height } + fadeOut())
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically { height -> -height } + fadeIn() with (slideOutVertically { height -> height } + fadeOut())
                }
            }
        ) { targetCount ->
            Text(text = "$targetCount")
        }

        Button(onClick = { count++ }) {
            Text("Add")
        }
    }
}

/** 演示 AnimatedContent + ContentTransform +  SizeTransform 的使用 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentTextSizeTransform() {
    Column {
        /*
        在使用 ContentTransform 来创建自定义过渡动画的同时，还可以使用 using 操作符连接 SizeTransform。SizeTransform 可以使我们
        预先获取到 currentContent 和 targetContent 的 Size 值，并允许我们来定制尺寸变化的过渡动画效果。

        下面例子中，targetContent 是一个小尺寸的 icon, targetContent 是一段大尺寸的文本，从 icon 到文本的切换过程中，可以使用
        SizeTransform 实现尺寸变化的过渡动画。在 SizeTransform 中可以通过关键帧 keyframes 指定 Size 在某一个时间点的尺寸，以及对应的动画时长。

        比如下面例子中表示 expend 过程持续时间为 300ms，
            在 150ms 前，高度保持不变，宽度逐渐增大；
            而在到达 150ms 之后，宽度到达目标值将不再变化，高度再逐渐增大
         */
        var expanded by remember { mutableStateOf(false) }
        Surface(
            color = MaterialTheme.colorScheme.primary,
            onClick = { expanded = !expanded }
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500, 500)) with fadeOut(animationSpec = tween(500)) using
                            SizeTransform { initialSize, targetSize ->
                                if (targetState) {
                                    keyframes {
                                        // Expand horizontally first.
                                        IntSize(targetSize.width, initialSize.height) at 500
                                        durationMillis = 1000
                                    }
                                } else {
                                    keyframes {
                                        // Shrink vertically first.
                                        IntSize(initialSize.width, targetSize.height) at 500
                                        durationMillis = 1000
                                    }
                                }
                            }
                }
            ) { targetExpanded ->
                if (targetExpanded) {
                    Text(
                        modifier = Modifier.size(200.dp),
                        text = "SizeTransform defines how the size should animate between the initial and the target contents. You have access to both the initial size and the target size when you are creating the animation. SizeTransform also controls whether the content should be clipped to the component size during animations."
                    )
                } else {
                    Box(Modifier.padding(10.dp)) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Localized description", Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

/**
补充：

1. 定义子元素动画：与 AnimatedVisibility 一样，AnimatedContent 内部的子组件也可以通过 Modifier.animatedEnterExit 单独指定动画。【参考 [AnimatedVisibilitySpecialChildren]】
2. 自定义 Enter/Exit 动画：通过 AnimatedContent 的定义可知，其 content 同样是在 AnimatedVisibilityScope 作用域中，所以内部也可以通过 transition 添加额外的自定义动画。【参考 [AnimatedVisibilityCustomized]】
 */
@Composable
fun AnimatedContentOthers() {

}