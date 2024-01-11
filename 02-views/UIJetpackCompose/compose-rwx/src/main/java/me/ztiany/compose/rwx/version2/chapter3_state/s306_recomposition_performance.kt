package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import timber.log.Timber

@Composable
fun S306_RecompositionPerformance() {
    var name by remember {
        mutableStateOf("Jordan")
    }

    // 由于 Column 是 inline 的，所以其没有形成独立的重组区域，内部 Text 的重组，也会直接导致 Column 所在区域的重组。
    Column {
        Button(onClick = {
            name = "Michael"
        }) {
            // HeavyComposable 独自形成一个重组区域，其不接受任何参数，Compose 会智能地判断出它不需要任何重组。
            HeavyComposable()
            /*
             HeavyComposableWithString 独自形成一个重组区域，其接受一个参数，Compose 会根据每次重组时传入的参数
             智能地判断出它不需要任何重组。Compose 如何判断？
              1. 参数如果不是 Stable 的，不需要判断，总是需要更新。【不可变的类是 Stable 的】
              2. Compose 判断参数有没有改变是判断的内容（即调用的 equals）。
             */
            HeavyComposableWithString(name)
            Text(text = "Click ME!")
        }
    }

}

@Composable
private fun HeavyComposable() {
    Timber.d("HeavyComposable() called")
    Text(text = "I am very HEAVY! don't update me frequently.")
}

@Composable
private fun HeavyComposableWithString(name: String) {
    Timber.d("HeavyComposableWithString() called with: name = $name")
    Text(text = "I am very HEAVY! don't update me frequently.")
}


/** 稳定的 user */
private data class StableUser(val name: String)

/** 如果所有的字段都是用 by mutableStateOf 这种形式的，Compose 认为它是 Stable 的。*/
private class ProxyStableUser(name: String, address: String) {
    var name by mutableStateOf(name)
    var address by mutableStateOf(address)
}

/** 不稳定的 user，name 是可变的。*/
private data class UnstableUser(var name: String)

/** 通过 @Stable 告诉 Compose 编译器，把它当作 Stable的。但是下面这种实现是不合理的，因为 name 的修改无法被监听。
 *
 * 使用 @Stable 注解的类需要满足下面要求：
 *
 *   1) The result of [equals] will always return the same result for the same two instances.
 *   2) When a public property of the type changes, composition will be notified.【Compose 编译器只对这一点做判断】
 *   3) All public property types are stable.
 */
@Stable
private data class MarkedStableUser(var name: String)

@Composable
private fun HeavyComposableWithStableUser(user: StableUser) {
    Timber.d("HeavyComposableWithStableUser() called with: user = $user")
    Text(text = "I am very HEAVY! don't update me frequently.")
}

@Composable
private fun HeavyComposableWithUnstableUser(user: UnstableUser) {
    Timber.d("HeavyComposableWithUnstableUser() called with: user = $user")
    Text(text = "I am very HEAVY! don't update me frequently.")
}

@Composable
private fun HeavyComposableWithMarkedStableUser(user: MarkedStableUser) {
    Timber.d("HeavyComposableWithMarkedStableUser() called with: user = $user")
    Text(text = "I am very HEAVY! don't update me frequently.")
}

/**
 * 下面的例子中，由于 Company 不是稳定的，其实导致了 StableUserBug 类也不是稳定的，但是 Compose 的编译器只会判断
 * 到 StableUserBug 的字段这一层，所以 Compose 编译器把它当作为稳定的。
 */
private class StableUserBug(name: String, company: Company) {
    var name by mutableStateOf(name)
    var company by mutableStateOf(company)
}

@Immutable
private class Company(var address: String)
