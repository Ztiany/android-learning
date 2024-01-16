package me.ztiany.compose.learn.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ztiany.compose.R

sealed class SwitchState {
    object OPEN : SwitchState()
    object CLOSE : SwitchState()
}

/** 演示 Transition 的使用*/
@Composable
fun SwitchBox() {
    var selectedState: SwitchState by remember {
        mutableStateOf(SwitchState.CLOSE)
    }

    /*
    Transition 也是低级别动画API中的一类。AnimateState 以及 Animatable 都是针对单个目标值的动画，而 Transition 可以面向多个目标值应用动画并保持它们同步结束。
    Transition 的作用更像是传统视图体系动画中的 AnimationSet。虽然这里的 Transition 与高级动画 API 中出现的 EnterTransition 和 ExitTransition 等在名字上很容易混淆，
    但实际是不同的东西。

    下面使用 updateTransition 基于某个状态创建一个 Transition 实例。updateTransition接收两个参数。
            - targetState 最重要，它是动画执行所依赖的状态。
            - label 是代表此动画的标签，可以在 Android Studio 动画预览工具中标识动画。
     */
    val transition = updateTransition(targetState = selectedState, label = "switch_transition")

    // 获取到 Transition 实例后，可以创建 Transition 动画中的所有属性状态。
    // 使用 animate* 来声明每个动画属性其在不同状态时的数值信息，当 Transition 所依赖的状态发生改变时，其中每个属性状态都会得到相应的更新。
    val selectBarPadding by transition.animateDp(
        // 可以为 animate* 设置 transitionSpec 参数，为属性状态制定不同的 AnimationSpec。
        transitionSpec = { tween(1000) }, label = ""
    ) {
        when (it) {
            SwitchState.CLOSE -> 40.dp
            SwitchState.OPEN -> 0.dp
        }
    }

    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(1000) }, label = ""
    ) {
        when (it) {
            SwitchState.CLOSE -> 1F
            SwitchState.OPEN -> 0F
        }
    }

    Box(modifier = Modifier
        .size(150.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable {
            selectedState = if (selectedState == SwitchState.CLOSE) SwitchState.OPEN else SwitchState.CLOSE
        }) {

        Image(
            painter = painterResource(id = R.drawable.img_scenery_a),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "点我",
            fontSize = 30.sp,
            fontWeight = FontWeight.W900,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(textAlpha)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(40.dp)
                .padding(top = selectBarPadding)
                .background(Color(0xFF5FB878))
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(1 - textAlpha)
            ) {
                Icon(Icons.Rounded.Star, contentDescription = "star", tint = Color.White)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "已选择",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W900,
                    color = Color.White
                )
            }
        }
    }

}

enum class DialerState {
    DialerMinimized,
    NumberPad
}

/**
 * 演示 createChildTransition 的使用：
 *  Transition 可以使用 createChildTransition 创建子动画，比如在下面的场景中。我们希望通过 Transition 来同步控制 DialerButton 和 NumberPad 的显隐，但是对于
 *  DialerButton 和 NumberPad 来说，各自只需要关心自己的状态。通过 createChildTransition 将 DialerState 转换成 Boolean 类型 State，能够更好地实现关注点分离。
 *  子动画的动画数值计算来自于父动画，某种程度上说，createChildTransition 更像是一种 map。
 */
@OptIn(ExperimentalTransitionApi::class)
@Composable
fun ChildTransitionSample() {
    var dialerState by remember { mutableStateOf(DialerState.NumberPad) }

    Box(Modifier.fillMaxSize()) {
        val parentTransition = updateTransition(dialerState, label = "parentTransition")

        // Animate to different corner radius based on target state
        val cornerRadius by parentTransition.animateDp(label = "cornerRadius") {
            if (it == DialerState.NumberPad) 0.dp else 20.dp
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .widthIn(50.dp)
                .heightIn(50.dp)
                .clip(RoundedCornerShape(cornerRadius))
        ) {

            NumberPad(
                // Creates a child transition that derives its target state from the parent
                // transition, and the mapping from parent state to child state.
                // This will allow:
                // 1) Parent transition to account for additional animations in the child
                // Transitions before it considers itself finished. This is useful when you
                // have a subsequent action after all animations triggered by a state change
                // have finished.
                // 2) Separation of concerns. This allows the child composable (i.e.
                // NumberPad) to only care about its own visibility, rather than knowing about
                // DialerState.
                visibilityTransition = parentTransition.createChildTransition {
                    // This is the lambda that defines how the parent target state maps to
                    // child target state.
                    it == DialerState.NumberPad
                }
                // Note: If it's not important for the animations within the child composable to
                // be observable, it's perfectly valid to not hoist the animations through
                // a Transition object and instead use animate*AsState.
            )

            DialerButton(
                visibilityTransition = parentTransition.createChildTransition {
                    it == DialerState.DialerMinimized
                },
                modifier = Modifier.matchParentSize()
            )

        }
    }
}

@Composable
private fun DialerButton(visibilityTransition: Transition<Boolean>, modifier: Modifier) {

    val scale by visibilityTransition.animateFloat(label = "scale") { visible ->
        if (visible) 1f else 2f
    }

    Box(
        modifier
            .scale(scale)
            .background(Color.Black)
    ) {
        // Content goes here
    }
}

@Composable
private fun NumberPad(visibilityTransition: Transition<Boolean>) {
    // Create animations using the provided Transition for visibility change here...
}

/**
 * 演示：Use transition with AnimatedVisibility and AnimatedContent：AnimatedVisibility 和 AnimatedContent 有针对 Transition 的扩展函数，
 * 将 Transition 的 State 转换成所需的 TargetState。借助这两个扩展函数，可以将 AnimatedVisibility 和 AnimatedContent 的动画状态通过 Transition 对外暴露，以供使用。
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TransitionWithAV_AC() {
    var selected by remember { mutableStateOf(false) }

    // Animates changes when `selected` is changed.
    val transition = updateTransition(selected, label = "transition")
    val borderColor by transition.animateColor(label = "borderColor") { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp(label = "elevation") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }

    Surface(
        onClick = { selected = !selected },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Hello, world!")
            // AnimatedVisibility as a part of the transition.
            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text(text = "It is fine today.")
            }

            // AnimatedContent as a part of the transition.
            transition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Selected")
                } else {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                }
            }
        }
    }
}

enum class BoxState { Collapsed, Expanded }

/**
 * 封装并复用 Transition 动画：在简单的场景下，在用户界面中使用 UpdateTransition 创建 Transition 并直接操作它完成动画是没有问题的。
 * 然而，如果需要处理一个具有许多动画属性的复杂场景，可能希望把 Transition 动画的实现与用户界面分开。可以通过创建一个持有所有动
 * 画值的类和一个返回该类实例的“更新”函数来做到这一点。Transition 动画的实现被提取到单独的函数中，便于后续进行复用。
 */
@Composable
fun EncapsulateTransition() {
    val boxState = remember {
        mutableStateOf(BoxState.Collapsed)
    }
    val transitionData = updateTransitionData(boxState)
    // UI tree
    Column {
        Box(
            modifier = Modifier
                .background(transitionData.color)
                .size(transitionData.size)
        )

        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (boxState.value == BoxState.Expanded) {
                boxState.value = BoxState.Collapsed
            } else {
                boxState.value = BoxState.Expanded
            }
        }) {
            Text(text = "Click ME!")
        }
    }
}

// Holds the animation values.
private class TransitionData(
    color: State<Color>,
    size: State<Dp>
) {
    val color by color
    val size by size
}

// Create a Transition and return its animation values.
@Composable
private fun updateTransitionData(boxState: MutableState<BoxState>): TransitionData {
    val transition = updateTransition(boxState, label = "transition")
    val color = transition.animateColor(label = "color") { state ->
        when (state.value) {
            BoxState.Collapsed -> Color.Gray
            BoxState.Expanded -> Color.Red
        }
    }
    val size = transition.animateDp(label = "size") { state ->
        when (state.value) {
            BoxState.Collapsed -> 64.dp
            BoxState.Expanded -> 128.dp
        }
    }
    return remember(transition) { TransitionData(color, size) }
}

/**
 *   rememberInfiniteTransition：InfiniteTransition 从名字上便可以知道其就是一个无限循环版的 Transition。一旦动画开始执行，便会不断循环下去，
 *   直至 Composable 生命周期结束。子动画可以用 animateColor、animatedFloat 或 animatedValue 等进行添加，另外还需要指定 infiniteRepeatableSpec
 *   来设置动画循环播放方式。
 */
@Composable
fun InfiniteTransition() {
    val infiniteTransition = rememberInfiniteTransition()

    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(Modifier.fillMaxSize().background(color))
}
