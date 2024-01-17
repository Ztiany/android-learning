package me.ztiany.compose.rwx.version1

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
动态 UI

    1. Compose 的动态 UI 使用的是一种叫做 MutableState 的东西，而不是手动地去 set。
    2. 我们通过 mutableStateOf(xxx) 来创建一个 MutableState，然后就可以使用它的 value 属性。
    3. 这是一个跨平台的方法，在 Java 平台的实现是 internal actual fun <T> createSnapshotMutableState()
    4. MutableState 的 value 属性是被定制过的，修改时可以通知使用者。
    5. 每次使用 MutableState.value 显得麻烦，因此 compose 还为 State 类型添加了 getValue/setValue 扩展方法，因此 MutableState 还支持被当作代理使用。

recompose 机制

    1. 在 setContent 方法内部创建 MutableState 然后立即使用，将丢失自动刷新功能，比如：
            var name by mutableStateOf("a")
            Text(name)
    2. 编译器在编译期间会对使用了 MutableState value 的代码重新包装一层，然后在 MutableState 的 value 被修改后重新执行那一行使用了 value 的代码。
       这个就叫 recompose 过程。也是是 ComposeUI 能自动响应修改的原因之一。
       而按照上面方式写，那么两行代码都会被包起来，导致 MutableState.value 修改后，MutableState 会被重新初始化。
       但是按照下面方式写又不会：
            var name by mutableStateOf("a")
            Button(onClick ={}){
                Text(name)
            }
       但是，不可能在实际开发中这样做，不过还有一个 remember{} 函数， 可以
            var name by remember{ mutableStateOf("a") }
            Button(onClick ={}){
                Text(name)
            }
       remember 方法用于防止某些变量因 recompose 后被重新初始化。
   3. @Composable 的方法都有可能被 recompose，因此内部创建的所有的 MutableState 都应该用 remember 包一层。

remember 函数的使用
    1. 带参数版本的 remember 的意思是用于给缓存加上 key，如果 key 不变，就会使用缓存。
    2. @Composable 的方法内才能使用 remember 函数。

State Hosting
    1. 内部有状态组件-->为了分享状态-->变为内部无状态组件【状态提取到外部】：其实就是在哪里定义 MutableState 的问题。
 */
@Composable
fun Lesson5_DynamicUI(name: MutableState<String>) {
    //使用的是 State 的 androidx.compose.runtime.getValue 扩展函数
    val nameValue by name
    //使用 Value
    Text(nameValue, Modifier.verticalScroll(rememberScrollState(0)))
}

@Composable
fun Lesson5_DynamicUI_Bad(scope: CoroutineScope) {
    //无法响应 name 的更新，因为 createDynamicName 被 recompose 过程重新初始化了。
    val nameValue by createDynamicName(scope)
    Text(nameValue, Modifier.verticalScroll(rememberScrollState(0)))
}

@Composable
fun Lesson5_DynamicUI_Good(scope: CoroutineScope) {
    //无法响应 name 的更新，因为 createDynamicName 被 recompose 过程重新初始化了。
    val nameValue by remember {
        createDynamicName(scope)
    }
    Text(nameValue, Modifier.verticalScroll(rememberScrollState(0)))
}

@Composable
fun Lesson5_DynamicUI_Remember(name: String) {
    //带参数版本的 remember 的意思是用于给缓存加上 key，如果 key 不变，就会使用缓存。
    val length = remember(name) {
        name.length
    }
    Text(length.toString(), Modifier.verticalScroll(rememberScrollState(0)))

}

fun createDynamicName(scope: CoroutineScope): MutableState<String> {
    return mutableStateOf("").also {
        scope.launch {
            var name = "1"
            while (true) {
                name += name
                delay(1000)
                it.value = name
            }
        }
    }
}