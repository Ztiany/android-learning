package me.ztiany.compose.foundation.gesture.pointerinput

import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp

/** 演示 forEachGesture 的使用*/
@Composable
fun ForEachGestureViews() {
    /*
    forEachGesture 存在的意义：Compose 手势操作实际上是在协程中监听处理的，当协程处理完一次手势交互后便会结束（比如一个 Down 或者一个 Move），当进行第二次手势交互时由于负责手势
    监听的协程已经结束，手势事件便会被丢弃掉。那么怎样才能让手势监听协程不断地处理一轮手势中的每一次交互呢？    我们很容易想到可以在外层嵌套一个 while(true) 进行实现，然而
    这么做并不优雅，且存在着一些问题。

            1. 当用户出现一连串手势操作时，很难保证各手势之间有清晰分界，即无法保证每一轮手势（Down-Move-Up）结束后，所有手指都是离开屏幕的（因为有一个 while 一直在转）。
            在传统 View 体系中，手指按下一次、移动到抬起过程中的所有手势事件可以看作是一个完整的手势交互序列。每当用户触摸屏幕交互时，可以根据这一次用户输入的手势交互序列中的信息
            进行相应的处理。
            2. 当第一轮手势处理结束或者被中断取消后，如果采用 while(true)，当第一轮手势因发生异常而中断处理时，此时手势仍在屏幕之上，则可能会影响第二轮手势处理，导致出现不
            符合预期的行为处理结果。
            3. Compose 为我们提供了 forEachGesture 方法，保证了每一轮手势处理逻辑的一致性。实际上的 GestureDetect 系列 API，其内部实现都使用了 forEachGesture。

通过 forEachGesture 的源码可知，每一轮手势处理结束后，或本次手势处理被取消时，都会使用 awaitAllPointersUp() 保证所有手指均已抬起，并且同时也会与当前组件的生命周期对齐，
当组件离开视图树时，手势监听也会随之结束。
     */

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = """
            forEachGesture 存在的意义：Compose 手势操作实际上是在协程中监听处理的，当协程处理完一次手势交互后便会结束（从 DOWN 到 UP），当进行第二次手势交互时由于负责手势监听的协程已经结束，    手势事件便会被丢弃掉。那么怎样才能让手势监听协程不断地处理每一轮的手势交互呢？    我们很容易想到可以在外层嵌套一个 while(true) 进行实现，然而这么做并不优雅，且存在着一些问题。
    
    
                1. 当用户出现一连串手势操作时，很难保证各手势之间有清晰分界，即无法保证每一轮手势结束后，所有手指都是离开屏幕的。在传统 View 体系中，手指按下一次、移动到抬起过程中的所有手势事件可以看作是一个完整的手势交互序列。每当用户触摸屏幕交互时，可以根据这一次用户输入的手势交互序列中的信息进行相应的处理。
                2. 当第一轮手势处理结束或者被中断取消后，如果采用 while(true)，当第一轮手势因发生异常而中断处理时，此时手势仍在屏幕之上，则可能会影响第二轮手势处理，导致出现不符合预期的行为处理结果。
                3. Compose 为我们提供了 forEachGesture 方法，保证了每一轮手势处理逻辑的一致性。实际上上面的 GestureDetect 系列 API，其内部实现都使用了 forEachGesture。


            通过 forEachGesture 的源码可知，每一轮手势处理结束后，或本次手势处理被取消时，都会使用 awaitAllPointersUp() 保证所有手指均已抬起（一轮的手势已经结束，从 Down 到 Move 再到 Up），
            并且同时也会与当前组件的生命周期对齐，当组件离开视图树时，手势监听也会随之结束。
    
        """.trimIndent(), fontSize = 12.sp, modifier = Modifier.pointerInput(Unit) {
                forEachGesture {

                }
            }
        )
    }

}
