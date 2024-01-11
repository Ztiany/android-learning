package me.ztiany.compose.rwx.version2.chapter3_state

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


/* derivedStateOf 可以防止因为状态频繁更新而导致的频繁重组。核心思想是就是添加一个类似 Flows distinctUntilChanged() operator 的机制。*/
@Composable
fun S307_DerivedStateOf() {
    var name by remember {
        mutableStateOf("Jordan")
    }

    // 不会缓存
    val uppercaseName1 = remember(name) {
        name.uppercase()
    }

    // 会缓存结果，只有计算的结果发生了变化才会触发更新。【因此更优】
    val uppercaseName2 = remember {
        derivedStateOf {
            name.uppercase()
        }
    }

    Button(onClick = {
        name = "JordaN"
    }) {
        Text(text = "Click ME!")
    }

    /*
    这里传递了参数 name，会在这里记录对 StateObject 的行为，而在 S307_SceneToUseRemember 内部，已经是一个纯粹的字符串
    了，没有 StateObject 的特性了。
     */
    S307_SceneToUseRemember(name) {
        name = "JordaN"
    }
}

@Composable
fun S307_TheProblemOfRemember() {
    /*
     remember 的 key 用于确定重组时是否需要更新其缓存的对象，如果两次 key 一致则不更新，这个一致性判断是使用 equals 方法的

     remember 对 key 类型为 MutableState 的参数没有特殊处理，也就说 remember 内部不会订阅类型为 MutableState 的参数的写操作。
     */
    val names = remember {
        mutableStateListOf("Micheal", "Jordan")
    }

    // 这种用法是错误的，重组时，remember 无法确认 names 是否变化了，因为它是用的同一对象来对比。
    val filtered1 = remember(names) {
        names.map { it.uppercase() }
    }

    // 这时应该使用 derivedStateOf 了
    val filtered2 = remember {
        derivedStateOf { names.map { it.uppercase() } }
    }

    // 因此结论是：对于那些带内部状态的对象，即通过方法而不是赋值操作符来更新的对象，用 `remember(object){}` 是不合适的，原因是 remember 无法检测到 Key 是否更新了。
}

@Composable
private fun S307_SceneToUseRemember(name: String, onClick: () -> Unit) {
    val uppercaseName1 = remember(name) {
        name.uppercase()
    }

    // 这种情况下，就不应该使用 derivedStateOf 了。
    val uppercaseName2 = remember {
        derivedStateOf {
            name.uppercase()
        }
    }

    Button(onClick = onClick) {
        Text(text = "Click ME!")
    }
}