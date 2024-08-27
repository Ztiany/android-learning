package me.ztiany.compose.rwx.version2.chapter6_sideeffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

/*
    什么是副作用？
        1. 这个词源于医药学，指的是药物除了治疗疾病的主要作用之外，还会产生其他的作用。
        2. 在编程领域，副作用指的是函数或者表达式除了返回值之外，还会产生其他的作用。
            比如一个函数的执行修改了某个全局的变量，那么这个函数就产生了副作用。
            相比之下，纯函数就是指没有副作用的函数，它的执行结果只依赖于输入参数，不依赖于外部状态。

     Composable 函数应该避免产生副作用，因为 Composable 函数可能会被多次调用，且它们的调用是由 Compose 系统决定的，且是无法预测的，
     甚至某一次的组合流程不需要执行完就会因此状态变化而中断，这样就会导致副作用的执行时机不确定，从而产生不可预测的结果。

     但是基于现实的需求，又确实需要在 Composable 中执行读取和修改状态的操作，这时候就需要使用 SideEffect 函数来包裹这些副作用。

     Compose 团队提供了一些 SideEffect API，比如 LaunchedEffect、DisposableEffect、SideEffect 等。
     通过这些 API，我们可以在 Composable 函数中执行副作用，而且这些副作用的执行时机是可控的和可预测的。
 */
@Composable
fun S601_SideEffect() {
    // SIdeEffect 在每次成功重组时都会执行，所以不能用来处理那些耗时或者异步的副作用逻辑。
    SideEffect {

    }
}