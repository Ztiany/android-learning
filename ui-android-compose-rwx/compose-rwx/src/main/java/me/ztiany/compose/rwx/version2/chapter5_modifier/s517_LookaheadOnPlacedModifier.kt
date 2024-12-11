package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadLayout
import androidx.compose.ui.layout.LookaheadLayoutCoordinates
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

/*
    LookaheadOnPlacedModifier 与 OnPlacedModifier 的作用几乎是一样的。

    Lookahead 的意思是“向前看”，本质是从算法层面多向前查看几个元素，比如编译器
    的工作，对于 for 循环，编译器不仅仅是看到 for 就任务是要执行循环，而是还要向前看几个元素，
    从而确定是真的要指向循环，而不是某个以 for 开头的变量名。
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun S517_LookaheadOnPlacedModifier() {
    /*
    LookaheadLayout 有两个测量和布局的流程：
        - 一个前瞻性的流程，用于确定布局的大小。
        - 一个正式的流程，可以拿到前瞻性流程的结果，从而对布局进行调整。

    应用场景：用动画的方式在做界面切换的时候，可以对界面进行相对平滑地进行渐进式的过渡。

    LookaheadLayout 内部就是用了 LookaheadOnPlacedModifier。
     */
    LookaheadLayout(content = {

        Row(Modifier
            // 在 LookaheadLayout，就可以使用一个特定版本的 onPlaced 了，可以拿到前瞻性流程的结果。
            .onPlaced { lookaheadScopeCoordinates: LookaheadLayoutCoordinates, layoutCoordinates: LookaheadLayoutCoordinates ->

            }) {
        }

    }) { measurables: List<Measurable>, constraints: Constraints ->
        layout(0, 0) {

        }
    }

}