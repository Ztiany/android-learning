package me.ztiany.compose.rwx.version1

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/*
MutableState 的使用：

    除了 mutableStateOf，还有 MutableStateListOf，MutableStateMapOf

主动 recompose 和被动 recompose

    1. 被动 recompose，比如 Lesson06_MutableState 方法中，Text 的刷新会导致下面  numberList.forEach {} 也重新 recompose。
    2. Compose 的自身对被动 recompose 有优化机制，比如 Lesson06_Recompose_AutoOptimize 中的 HeavyText 不会被重复执行。

 */
@Composable
fun Lesson06_State() {
    Column {
        Lesson06_MutableState()
        Divider(Modifier.padding(0.dp, 10.dp))
        Lesson06_MutableStateList()
    }
}

@Composable
private fun Lesson06_MutableState() {
    val numberList by remember {
        mutableStateOf(mutableListOf(1, 2, 3))
    }

    val mutableNumberList = remember {
        mutableStateListOf(1, 2, 3)
    }

    var flag by remember {
        mutableStateOf(1)
    }

    Column {
        //Text 的 recompose 能触发下面布局的 recompose
        Text(text = flag.toString(), modifier = Modifier
            .padding(20.dp)
            .clickable {
                flag++
            })

        Button(onClick = {
            //无法起作用的原因是更新的是 list 这个对象，而不是 mutableState 的 value。
            numberList.add(Random.nextInt(10))
            mutableNumberList.add(Random.nextInt(10))
        }) {
            Text(text = "加")
        }

        numberList.forEach {
            Text(text = "第 $it 个文字块")
        }
    }

}

@Composable
private fun Lesson06_MutableStateList() {

    val mutableNumberList = remember {
        mutableStateListOf(1, 2, 3)
    }

    Column {
        Button(onClick = {
            mutableNumberList.add(Random.nextInt(10))
        }) {
            Text(text = "加")
        }

        mutableNumberList.forEach {
            Text(text = "第 $it 个文字块")
        }
    }

}

data class User(val name: String)

@Stable
data class User1(var name: String)

/*不用加 @Stable，因为 name 是 mutableStateOf 代理的的 */
class User2(name: String) {
    var name: String by mutableStateOf(name)
}

private var flagOutSide = 10
private var user = User("Alien")

@Composable
fun Lesson06_Recompose_AutoOptimize() {

    val numberList by remember {
        mutableStateOf(mutableListOf(1, 2, 3))
    }

    val mutableNumberList = remember {
        mutableStateListOf(1, 2, 3)
    }

    var flag by remember {
        mutableStateOf(1)
    }

    Column {
        //Text 的 recompose 能触发下面布局的 recompose
        Text(text = flag.toString(), modifier = Modifier
            .padding(20.dp)
            .clickable {
                flag++
                //修改 flagOutSide 变量，则的 HeavyText_Name 被动 Recompose 的优化失败
                flagOutSide += flagOutSide
                //修改 user 变量，但是内容一致，也不会导致 recompose
                user = User(user.name)
            })

        HeavyText()
        HeavyText_Name(flagOutSide.toString())
        HeavyText_User(user)

        Button(onClick = {
            //无法起作用的原因是更新的是 list 这个对象，而不是 mutableState 的 value。
            numberList.add(Random.nextInt(10))
            mutableNumberList.add(Random.nextInt(10))
        }) {
            Text(text = "加")
        }

        numberList.forEach {
            Text(text = "第 $it 个文字块")
        }
    }

}

/*
虽然 HeavyText 上面的那个 Text 会的更新导致 HeavyText 重新执行，但是：
 1. HeavyText 内部的代码是被 Compose 编译器重新包装了一层的。
 2. 而且 HeavyText 默认也会被编译器加上两个参数：Composer 和 id。如果多次调用，发现两个参数没有变，就会忽略。
 */
@Composable
fun HeavyText() {
    Log.d("Lesson06", "HeavyText 执行了很耗时的方法")
    Text("Heavy Text")
}

/*
是否调用 Composable 方法，取决于这个方法的任意一个参数是否发生变化，使用的是 == 来判断，即比较内容，而不是引用。
 */
@Composable
fun HeavyText_Name(name: String) {
    Log.d("Lesson06", "HeavyText_Name 执行了很耗时的方法")
    Text("Heavy Text name = $name")
}

/*
对于自定义的类型：
    1. 如果这个类型是可靠的类型，则使用 == 判断。
    2. 如果这个类型是不可靠的类型，则使用 === 判断。
    3. 属性都是 val 类型的变量才是可靠的类型。【如果 User 的 name 属性为 var 修饰，则 Compose 不会优化 HeavyText_User 的 recompose】
    4. 为什么用 var 修饰属性的类型就不是可靠的类型呢？因为 var 变量是可变的，虽然现在没有修改，但是无法保证之后它不被改变，而全是 val 的类型，是不可变的。
    5. 同理类比 HashMap，作为 HashMap 的 Key 的类型应该确保这个类型参入计算 hashcode 的属性是不可变的。
    6. 使用 @Stable 注解可以不可靠的类型标识为可靠的类型。
 */
@Composable
fun HeavyText_User(user: User) {
    Log.d("Lesson06", "HeavyText_User 执行了很耗时的方法")
    Text("Heavy Text user = $user")
}
