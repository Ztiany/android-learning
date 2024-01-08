# [《Jetpack Compose：从上手到进阶再到高手》](https://edu.rengwuxian.com/p/t_pc/goods_pc_detail/goods_detail/course_2Dpw6101YdL7bHFs5LFpYyzSUS6?app_id=appuoykwnbg2526) 学习记录

## 1 用 Compose 写一个文学解码

参考：[compose-wechat](../compose-wechat)

## 2 Compose 关键知识与概念详解

### 2.1 什么是声明式 UI

**声明式 UI**：理解什么是声明式 UI，主要只需要通过声明来构建 UI，杜绝命令式的 UI  交互，一切以数据为驱动，参考 [声明式 UI？Android 官方怒推的 Jetpack Compose 到底是什么](https://rengwuxian.com/jetpack-compose-3/)。

不是不更新 UI，而是根据数据（状态）的变化，自动更新 UI。

### 2.2 从文字和图片到「独立于平台」的含义和未来

文本和图片为什么改名了？

- TextView --> Text
- Bitmap --> Image

因为独立于平台，不依赖于系统的原生控件，甚至不依赖于 Android。如何理解**独立于平台**：

1. Compose 独立于平台，不依赖于 Android 环境【暴露给开发者的 API 不依赖于 Android 环境，但是底层还是会使用到 Android 的相关 API，比如 Canvas】。
2. Compose 是完全重新设计的。
3. 正是因为 Compose 的独立于平台的特性，AndroidStudio 内置的预览功能非常强大，因为不需要依赖于 Android 的 API。

在底层依然依赖于 Canvas，但是在 API 层绝对是独立于平台的，因此 AndroidStudio 也可以写一套底层 API 来解析并预览 Compose 写的界面。另外，基于 JetBrains 的支持，**Compose 也开始支持 Mac、Windows、Linux**。【利益关系：Compose 推广开来了，Kotlin 的使用者也就多了】

关于 Image 组件：

1. 支持位图：ImageBitmap
2. 支持矢量图：ImageVector

另外也可以提供一个 Painter 对象（类比 Drawable），使用 `painterResource()` 可以快速创建 Painter 对象。

**第三方库**：[Accompanist](https://github.com/google/accompanist) 这个库提供了很多辅助功能，首先由 ChrisBanes，后续 ChrisBanes 离开 Google，但是将该项目过继给了 Google。Compose 的字面意思有“作曲”的意思，Accompanist 的意思是伴奏者，相对于 Compose 标准库中的代码，虽然 Accompanist 中的功能也很重要，但是没有那么稳定，可以认为其是新功能的孵化器。

对于网络图片的加载和显示，Accompanist 起初包装了 Glide、Coil 等常用的图片加载库，后续 Coil 官方提供了对 Compose 的支持，因此 Accompanist 中便移除了相关代码。开发者现在直接使用 Coil 即可。

```groovy
implementation "com.google.accompanist:accompanist-coil:0.15.0"
```

具体的使用方式，参考 <https://coil-kt.github.io/coil/compose/>。

### 2.3 传统 Layout 的 Compose 平替

- FrameLayout ---- Box
- LinearLayout ---- Colum/Row
- RelativeLayout ---- Box + Modifier 控制位置
- ConstraintLayout/MotionLayout ---- Box【简单布局】/ ConstraintLayout 的 Compose 版【复杂布局】
- ScrollView ---- 使用 Modifier.verticalScroll() 等 API
- RecyclerView ---- LazyColum
- ViewPager --- Pager
- ...

### 2.4 Modifier 的几个特点

Modifier 可以用来调整布局熟悉。

#### 没有 Margin

传统 View 中：

- padding 表示内边距
- margin 表示外边距

Compose 里面只有 padding 没有 margin【padding 和 margin 的区分为是否绘制背景色】，但是 Compose 里面，Modifier 对函数调用顺序是敏感的，比如：

```kotlin
Modifier.padding(8.dp).background(Color.Black)//这里 padding 影响背景。【这其实就是相当于 margin 的效果】
Modifier.background(Color.Black).padding(8.dp)//这里 padding 不影响背景。
Modifier.padding(8.dp).clickable {}//这里 padding 区域不响应点击。【这其实就是相当于 margin 的效果】
Modifier.clickable {}.padding(8.dp)//这里 padding 区域响应点击。
```

除此之外，对 Modifier 相同 API 的多次调用，效果可能是叠加的。总之对 Modifier 的调用是顺序敏感的。

#### 默认的 size

Compose 中，不强制要求写组件的 size，不写则默认行为类似于传统 View 的 wrap-content。

#### 什么时候用 Modifier

Modifier 用于做通用的属性设置（比如宽高、点击、背景色等），而控件特有的特性应该是用在控件函数的参数中去设置（比如 Text 的 fontSize）。

但是 Button 有一个 onClick 参数，不过这也不是搞特殊，本质上还是用的 Modifier，目的是为了方便开发者，Button 本就是用来做点击的。

#### 声明式 UI 的特点

拿不到控件对象，比如 Text 是一个方法，没有返回值，你拿不到表示文本的那个对象，也就不可能拿这个对象去做样式修改，这就是声明式 UI，一切通过声明的方式来设置。

### 2.6 从按钮到 MD，Compose 分包

[compose](https://developer.android.com/jetpack/androidx/releases/compose?hl=zh-cn) 由 androidx 中的 6
个 Maven 组 ID 构成。每个组都包含一套特定用途的功能，并各有专属的版本说明。

为什么要进行分组呢？ 为了方便扩展，进行分层设计，这对于持续的更新来说，是非常方便的。【另外一方面也是从传统 View 系统的无分组设计导致比较高的耦合性，学习到的经验教训】

分层说明（从下到上）：

1. 底层组件：`compose.compiler`，这个组件不需要明确配置，是 Compose 在编译期的插件。
2. `compose.runtime` 是 Compose 最底层的设计，比如通用的数据结构、状态转换机制等。代表性 API：remember。
3. `compose.ui` 是 Compose 最基础的 UI 支持，比如基本组件、测量布局绘制、触摸反馈等。代表性 API：layout。
4. `compose.animation` 是 Compose 对动画的支持。
5. `compose.foundation` 提供了常用的具体的控件，相对完整的 UI 体系【没有 foundation 也能使用 Compose，但是有了就更方便，因为 foundation 对很多常见功能都提供了实现，开发者也可以基于 foundation 来扩展，设计自己特定风格的系统元素】。
6. `compose.material` 是 google 提供的基于 Compose 的 Material2 风格化组件。【是 Google 提供的一套带有特性设计风格的组件，开发者可以不使用 Material2】
7. `compose.material3` 是 google 提供的基于 Compose 的 Material3（也叫 Material You） 风格化组件。【是 Google 提供的一套带有特性设计风格的组件，开发者可以不使用 Material3】

查看 Material 提供的组件：

- Material2：<https://m2.material.io/components>
- Material3：<https://m3.material.io/components>

Compose 的其他扩展组件：

1. 比如 `ui` 组件依赖了 `ui.util`，所以不需要明确依赖 `ui.util`。
2. `ui-tooling` 提供了预览支持，其依赖于 `ui-tooling-preview` 组件，而 `ui-tooling-preview` 组件又依赖于 `ui` 组件。【如果需要预览功能，就要声明该组件】
3. `material` 的两个扩展组件：`material-icons-core`（material 组件依赖了该组件）和 `material-icons-extended` 提供了一些 MD 的常用图标，其中 `material-icons-extended` 是可选的。

## 3 状态订阅于自动更新

### 3.1 自定义 Composable

所有的 Composable 函数都需要加上 @Composable 注解，这个注解会被编译器处理，加上一些内部代码，这些代码是实现 ComposeUI 的关键。

> 编译器插件相比 APT/修改字节码 功能更强大，而且是跨平台的。

Compose 的编译器插件（Compiler Plugin）为开发者做了很多事情，编译器插件通过 `@Composable` 注解来识别需要处理的函数。 内置的 `Composer.invokeComposable` 方法是 Compose 的入口，即真正调用 composable 方法的地方。

```kotlin

```

**Composable 注解**：

1. Composable 是一个标识符，用于告诉编译器插件这个方法用于生成界面，编译器插件会对这些方法做一些修改。
2. 加了 Composable 的方法必须在别的加了 Composable 方法中调用。
3. 不用于构建界面的函数就没必要加 Composable 注解了，没有作用，反而增加编译器的负担。

**自定义 Composable 相当于什么**？所谓自定义 Composable 其实就是编写带有 `@Composable` 注解的函数来描述 UI。 就相当于 xml 布局文件 + 自定义 View/ViewGroup。既可以有布局又可以有逻辑，即具有 xml 简单直观的特点，又可以加上逻辑。

### 3.2 MutableState 和 `mutableStateOf()`

在 Compose 中，UI 通过订阅 MutableState 类型的变量来实现自动感知数据变更并重绘的功能。MutableState 的创建方式如下，使用 `mutableStateOf("A")` 函数即可创建一个 MutableState。

如下所示，如果改变 `text.value` 的值，将会导致 Text 的重组，那么这是如何实现的呢？

```kotlin
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = mutableStateOf("A")
        
        setContent {
            Text(text = text.value)
        }
    }
        
}
```

当需要修改 Text 呈现的文字时，只需要修改 text.value 的值即可，Text 会自动更新。

Compose 整体的绘制流程：组合（重组）-->布局（测量 + 布局）-->绘制。 其中组合过程就是拼凑出界面实际内容的过程，其实就是执行我们编写的 Compose 函数的过程。

代码过程：ComposeView-->AndroidComposeView-->LayoutNode-->LayoutNode...

相关术语：

- 过程：Compose
- 结果：Composition

#### 自动更新原理简介

MutableState->StateObject->StateRecord->Compose 支持事务功能。

```kotlin
internal open class SnapshotMutableStateImpl<T>(
    value: T,
    override val policy: SnapshotMutationPolicy<T>
) : StateObject, SnapshotMutableState<T> {
    
    @Suppress("UNCHECKED_CAST")
    override var value: T
        // 关键在 readable，里面会记录当前变量被使用了（即订阅这个状态的后续更新）。即 snapshot.readObserver?.invoke(state) 所执行的工作。
        // 另外，readable 内部会遍历 StateRecord 链表，找到最新且可用的那个值。
        get() = next.readable(this).value
        // 这里面也会调用 readable 函数，但是这个版本是只取值，不订阅。
        // 这里会拿到最新的可用的值，并更新为传入的 value。
        set(value) = next.withCurrent {
            if (!policy.equivalent(it.value, value)) {
                next.overwritable(this, it) { this.value = value }
            }
        }

    private var next: StateStateRecord<T> = StateStateRecord(value)


    // 链表结构的头节点，做成链表是为了支持事务功能，Compose 也需要记住旧的状态，因为重组是可以回滚的。
    override val firstStateRecord: StateRecord
        get() = next
    
    //...
    
}

// ==========================================================
// Snapshot
// ==========================================================

fun <T : StateRecord> T.readable(state: StateObject): T {
    val snapshot = Snapshot.current
    // 读的时候，记录在哪里读了，即订阅。
    snapshot.readObserver?.invoke(state)
    return readable(this, snapshot.id, snapshot.invalid) ?: sync {
        // Readable can return null when the global snapshot has been advanced by another thread
        // and state written to the object was overwritten while this thread was paused. Repeating
        // the read is valid here as either this will return the same result as the previous call
        // or will find a valid record. Being in a sync block prevents other threads from writing
        // to this state object until the read completes.
        val syncSnapshot = Snapshot.current
        @Suppress("UNCHECKED_CAST")
        readable(state.firstStateRecord as T, syncSnapshot.id, syncSnapshot.invalid) ?: readError()
    }
}

internal inline fun <T : StateRecord, R> T.overwritable(
    state: StateObject,
    candidate: T,
    block: T.() -> R
): R {
    var snapshot: Snapshot = snapshotInitializer
    return sync {
        snapshot = Snapshot.current
        this.overwritableRecord(state, snapshot, candidate).block()
    }.also {
        // 通知更新
        notifyWrite(snapshot, state)
    }
}

@PublishedApi
internal fun notifyWrite(snapshot: Snapshot, state: StateObject) {
    // 两套订阅系统，两个功能：
    //      1. 查找这个变量在哪里被读了，然后把这部分的组合结果（Composition）标记为失效，下次重组就会更新。
    //      2. 这里其实是对写做了订阅。
    snapshot.writeObserver?.invoke(state)
}
```

数据类型说明：

- StateRecord 记录了每个变量修改前后的值，其实是一个链表结构，即 Compose 记录每个变量的每个状态，用的是 StateRecord。
- 具体各个链表上的哪些节点，它们共属于同一个状态（快照）也有记录，这个记录就是用 Snapshot 做的。
- 系统有多个 Snapshot 的时候，它们是有先后关系的，Snapshot 对应的是整个 Compose 的状态（某个时间点的快照）。
- 同一个 StateObject 的每个 StateRecord，都有它们对应的 Snapshot 的 id。StateRecord 和 Snapshot 就算不直接对应，只要 StateRecord 的 Snapshot 对另一个是有效的，另一个就能取到这个 StateRecord。

其实 Compose 有两套订阅系统，分别针对不同对象来做的订阅。

- 首先，Compose 要先对 Snapshot 的读写行为分别进行订阅，即对它对读写每一个 StateObject 的行为做订阅。这样当我们读写 StateObject 时，就会收到相应的通知，通知的内容是某个对象被读或者被写了，被读写的是说，即 `snapshot.writeObserver?.invoke(state)` 里的 state。即我们会订阅 Snapshot 在里面的，对 StateObject 的读和写，并在读和写时去通知，这两个订阅的通知部分就是 `snapshot.writeObserver?.invoke(state)` 和 `snapshot.readObserver?.invoke(state)`，而订阅行为是在 Snapshot 被创建时就执行了的。
- 另外 Compose 还会对具体每一个 StateObject 的应用事件做订阅，即在每一个 StateObject 被读的时候记录它的位置。即 `snapshot.readObserver?.invoke(state)` 即做了对 Snapshot 的通知，也做了对这个 StateObject 被读的订阅，订阅的是对这个 StateObject 新值的应用事件。【什么是应用？Compose 里面存在多个 Snapshot，但是只能有一个当前且全局的 Snapshot，当对非当前的 Snapshot 的某个值的改动应用到全局的 Snapshot 时，这个值才是有效的，这个过程叫应用】什么时候会做应用事件通知呢？查看后续笔记。

总结：

- 1，对 Snapshot 中读写 StateObject 对象的订阅，分别订读和写，所以有两个接受者：readObserver 和 writeObserver。发生时间：订阅: Snapshot 创建的时候；通知：读和写的时候。
- 2，对每一个 StateObject 的应用做订阅。发生时间：订阅：第一个订阅的 readObserver 被调用 (通知)的时候；通知：StateObject 新值被应用的时候 (后面讲)。
 
注意：这两套订阅通知系统分别对应着不同场景下的自动更新 UI。

```kotlin
@Composable
private fun AutoUpdate() {
    val test = remember {
        mutableStateOf("1")
    }

    Box(Modifier.clickable {
        // 这个写事件没有发生在组合过程，这不会通知到 snapshot.writeObserver，这里的自动更新由【应用】事件实现。
        test.value = "2"
    }) {
        // 这个对 test 的读被记录
        Text(text = test.value)
        // 这个对 test 的写发生在组合过程中，这个会使得 snapshot.writeObserver 发送通知。这里就会将这个区域标记为需要更新。
        test.value = "3"
    }
}
```

对 MutableState 的操作：

- get：记录读
- set：标记失效
- 应用事件：标记失效

某个区域失效了，在下一次重组就会刷新 UI，具体的刷新行为是重组、重绘或者是布局根据情况而定。

### 3.3 重组作用域与 remember

Compose 的自动重组，需要在状体更新时，自动重新执行某一段代码，那就需要持有这段代码的句柄，Compose 是如何做到的呢？这就是 Compose 编译器的工作了，Compose 编译器会将所有可能被重新执行的代码区域做一层包装。

重组是智能的有一定范围的，范围越小越精细，对性能越好，这个范围也叫重组作用域。

用于 Compose 的代码块的会被重新执行，因此在里面初始化的变量也会被重新初始化。需要一个机制来确保这些变量只初始化一次（不如这个变量永远都是初始化值，那就没有任何意义了），这个防止变量被重新初始化的功能就是 remember 函数。

```kotlin
@Composable
private fun MutableState() {
    val text = remember {
        mutableStateOf("1")
    }

    Text(text = text.value)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            text.value = (text.value.toInt() + 1).toString()
        }
    }
}
```

我们没有办法判断哪些区别是否会被重组（即重新执行），那么在所有的 Composable 里面所有初始化变量，至少都应该使用 remember 包一层。

remember 其实就是起到一个缓存作用，不带参数的 remember 的缓存永远不会过期（直到当前 Composable 销毁），带参数的 remember 根据参数的不同来刷新缓存。

### 3.4 无状态、状态提升和单向数据流

#### 无状态与状态提升

无状态就是 Compose 这个声明式 UI 框架的特点。 状态就可以理解为控件的属性。

无状态指的是没有==内部状态==，我们可以把 Composable 写成无状态的，也可以写成有状态的。 

有时我们需要访问有状态的 Composable 的内部状态，我们可以把这个状态定义到外部，然后将外部状态传入到 Composable 内部（这时这个 Composable 就变成无状态的了），这样外部的状态就可以影响到内部 Composable 的展示。这样就实现了一种状态共享，这种将内部状态提升到外部状态的做法叫做状态提升（State Hoisting）。

比如 TextField 就是一个很好的例子：

```kotlin
@Composable
private fun StateHoisting() {
    // 状态
    val field = remember {
        mutableStateOf("")
    }

    // 无状态的 TextField
    TextField(value = field.value, onValueChange = {
        field.value = it
    })
}
```

#### Single Source of Truth

解为了多数据源的数据同步的问题，比如（同时从网络和本地数据库加载数据，如何确保始终展示最新的数据。），解决方法就是 Single Source of Truth。

Single Source of Truth 具体如何做呢？对多数据源进行分层，让其中一个数据源作为另一个数据源的数据源。比如同时从网络和本地数据库加载数据，应该让网络作为数据库的数据源。数据库作为 UI 层的唯一数据源。

这也是为什么 TextField 要这么设计的原因之一：

```kotlin
// TextField 只使用从 value 传入值，而对于新的输入，它通过回调交给外部处理，外部可以更新 value值，这就实现了 Single Source of Truth 原则。
TextField(value = field.value, onValueChange = {newValue->
    // 如果 TextField 内部自动处理 value 的更新，那就有了多个信息源了。
    // 比如参考下面逻辑：如果 TextField 自动展示输入的新值，再回调，那么就可能造成输入框内输入一个标点符号，然后突然又消失了。这就是多信息源的弊端。
    if(newValue.没有标点符号()){
        name = newValue
    }else{
        name = newValue.去掉标点符号()
    }
})
```

#### 单向数据流

通过实现 Single Source of Truth，我们也就实现了单向数据流（Unidirectional Data Flow）。

数据的变更总是只有一个方向，朝着一个方向向另一个传递。即：

- 数据由外部（上）向内部（下）传递；
- 事件由内部（下）向外部（上）传递。

从 TextField 的封装也可以看出，如果要对具有交互功能的组件做状态提升，也需要将交互事件通过回调暴露出来。

### 3.5 更新 List 竟然不会触发刷新？——状态机制的背后

### 3.6 重组的性能风险和只能优化、@Stable

### 3.7 derivedStateOf——和 remember 有什么区别？

### 3.8 CompositionLocal——是状态又不全是

## 4 动画


## 5 Modifier 全解析


理解 Modifier：

1. Modifier 的官方解释：An ordered, immutable collection of modifier elements that decorate or add behavior to Compose UI elements.
2. Modifier 用于添加 UI 元素的公共修饰和行为，比如背景色，点击事件。

Modifier 的操作：

1. Modifier 是一个解开，但是这个接口有一个 Companion 对象，也是 Modifier，这个可以认为是一个空的 Modifier，没有任何效果。
2. 每次级联操作，都会组合新的和旧的为一个全新的 Modifier，参考下面代码。

```kotlin
fun Modifier.background(
   color: Color,
   shape: Shape = RectangleShape
) = this.then(
   Background(
      color = color,
      shape = shape,
      inspectorInfo = debugInspectorInfo {
         name = "background"
         value = color
         properties["color"] = color
         properties["shape"] = shape
      }
   )
)

interface Modifier {

   fun <R> foldIn(initial: R, operation: (R, Element) -> R): R
   fun <R> foldOut(initial: R, operation: (Element, R) -> R): R
   fun any(predicate: (Element) -> Boolean): Boolean
   fun all(predicate: (Element) -> Boolean): Boolean
   
   //then 的作用就是组合
   infix fun then(other: Modifier): Modifier =
      if (other === Modifier) this else CombinedModifier(this, other)

   // 默认的 Modifier
   // The companion object implements `Modifier` so that it may be used  as the start of a
   // modifier extension factory expression.
   companion object : Modifier {
      override fun <R> foldIn(initial: R, operation: (R, Element) -> R): R = initial
      override fun <R> foldOut(initial: R, operation: (Element, R) -> R): R = initial
      override fun any(predicate: (Element) -> Boolean): Boolean = false
      override fun all(predicate: (Element) -> Boolean): Boolean = true
      override fun toString() = "Modifier"
      //直接返回 other，因为自己是个空实现，没必要组合
      override infix fun then(other: Modifier): Modifier = other
   }
   
}
```

Element：

1. 除了 Modifier 对象和 CombinedModifier，其他的 Modifier 都实现了 `Modifier.Element` 接口。
2. 下面四个方法虽然在 Modifier 中定义，但是不是作为通用功能创建的，而是为 CombinedModifier 服务的。

```kotlin
 interface Element : Modifier {
   
     //从左到右叠加
     override fun <R> foldIn(initial: R, operation: (R, Element) -> R): R =
         operation(initial, this)

     //展开
     override fun <R> foldOut(initial: R, operation: (Element, R) -> R): R =
         operation(this, initial)
   
     //判断某一个是否满足要求
     override fun any(predicate: (Element) -> Boolean): Boolean = predicate(this)

     //判断所有是否满足要求
     override fun all(predicate: (Element) -> Boolean): Boolean = predicate(this)
   
 }

//可以看到，CombinedModifier 内部方法都调用了 outer 和 inner 的实现。
class CombinedModifier(
   private val outer: Modifier,
   private val inner: Modifier
) : Modifier {
   override fun <R> foldIn(initial: R, operation: (R, Modifier.Element) -> R): R =
      inner.foldIn(outer.foldIn(initial, operation), operation)

   override fun <R> foldOut(initial: R, operation: (Modifier.Element, R) -> R): R =
      outer.foldOut(inner.foldOut(initial, operation), operation)

   override fun any(predicate: (Modifier.Element) -> Boolean): Boolean =
      outer.any(predicate) || inner.any(predicate)

   override fun all(predicate: (Modifier.Element) -> Boolean): Boolean =
      outer.all(predicate) && inner.all(predicate)

   override fun equals(other: Any?): Boolean =
      other is CombinedModifier && outer == other.outer && inner == other.inner

   override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

   override fun toString() = "[" + foldIn("") { acc, element ->
      if (acc.isEmpty()) element.toString() else "$acc, $element"
   } + "]"
}
```

LayoutNode 中的 setModifier 实现：

```kotlin
 /**
  * The [Modifier] currently applied to this node.
  */
 override var modifier: Modifier = Modifier
     set(value) {
         if (value == field) return
         if (modifier != Modifier) {
             require(!isVirtual) { "Modifiers are not supported on virtual LayoutNodes" }
         }
         field = value

         val invalidateParentLayer = shouldInvalidateParentLayer()

         copyWrappersToCache()
         markReusedModifiers(value)

         // Rebuild LayoutNodeWrapper
         val oldOuterWrapper = outerMeasurablePlaceable.outerWrapper
         if (outerSemantics != null && isAttached) {
             owner!!.onSemanticsChange()
         }
         val addedCallback = hasNewPositioningCallback()
         onPositionedCallbacks?.clear()

         // 倒序地进行转换，组合为 LayoutNodeWrapper
         // Create a new chain of LayoutNodeWrappers, reusing existing ones from wrappers
         // when possible.
         val outerWrapper = modifier.foldOut(innerLayoutNodeWrapper) { mod, toWrap ->
             var wrapper = toWrap
             if (mod is RemeasurementModifier) {
                 mod.onRemeasurementAvailable(this)
             }

             val delegate = reuseLayoutNodeWrapper(mod, toWrap)
             if (delegate != null) {
                 if (delegate is OnGloballyPositionedModifierWrapper) {
                     getOrCreateOnPositionedCallbacks() += delegate
                 }
                 wrapper = delegate
             } else {
                 // The order in which the following blocks occur matters. For example, the
                 // DrawModifier block should be before the LayoutModifier block so that a
                 // Modifier that implements both DrawModifier and LayoutModifier will have
                 // it's draw bounds reflect the dimensions defined by the LayoutModifier.
                 if (mod is DrawModifier) {
                     wrapper = ModifiedDrawNode(wrapper, mod)
                 }
                 if (mod is FocusModifier) {
                     wrapper = ModifiedFocusNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is FocusEventModifier) {
                     wrapper = ModifiedFocusEventNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is FocusRequesterModifier) {
                     wrapper = ModifiedFocusRequesterNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is FocusOrderModifier) {
                     wrapper = ModifiedFocusOrderNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is KeyInputModifier) {
                     wrapper = ModifiedKeyInputNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is PointerInputModifier) {
                     wrapper = PointerInputDelegatingWrapper(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is NestedScrollModifier) {
                     wrapper = NestedScrollDelegatingWrapper(wrapper, mod).assignChained(toWrap)
                 }
                 @OptIn(ExperimentalComposeUiApi::class)
                 if (mod is RelocationModifier) {
                     wrapper = ModifiedRelocationNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is RelocationRequesterModifier) {
                     wrapper = ModifiedRelocationRequesterNode(wrapper, mod)
                         .assignChained(toWrap)
                 }
                 if (mod is LayoutModifier) {
                     wrapper = ModifiedLayoutNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is ParentDataModifier) {
                     wrapper = ModifiedParentDataNode(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is SemanticsModifier) {
                     wrapper = SemanticsWrapper(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is OnRemeasuredModifier) {
                     wrapper = RemeasureModifierWrapper(wrapper, mod).assignChained(toWrap)
                 }
                 if (mod is OnGloballyPositionedModifier) {
                     wrapper =
                         OnGloballyPositionedModifierWrapper(wrapper, mod).assignChained(toWrap)
                     getOrCreateOnPositionedCallbacks() += wrapper
                 }
             }
             wrapper
         }

         outerWrapper.wrappedBy = parent?.innerLayoutNodeWrapper
         outerMeasurablePlaceable.outerWrapper = outerWrapper

         if (isAttached) {
             // call detach() on all removed LayoutNodeWrappers
             wrapperCache.forEach {
                 it.detach()
             }

             // attach() all new LayoutNodeWrappers
             forEachDelegate {
                 if (!it.isAttached) {
                     it.attach()
                 }
             }
         }
         wrapperCache.clear()

         // call onModifierChanged() on all LayoutNodeWrappers
         forEachDelegate { it.onModifierChanged() }

         // Optimize the case where the layout itself is not modified. A common reason for
         // this is if no wrapping actually occurs above because no LayoutModifiers are
         // present in the modifier chain.
         if (oldOuterWrapper != innerLayoutNodeWrapper ||
             outerWrapper != innerLayoutNodeWrapper
         ) {
             requestRemeasure()
         } else if (layoutState == Ready && addedCallback) {
             // We need to notify the callbacks of a change in position since there's
             // a new one.
             requestRemeasure()
         }
         // If the parent data has changed, the parent needs remeasurement.
         val oldParentData = parentData
         outerMeasurablePlaceable.recalculateParentData()
         if (oldParentData != parentData) {
             parent?.requestRemeasure()
         }
         if (invalidateParentLayer || shouldInvalidateParentLayer()) {
             parent?.invalidateLayer()
         }
     }
```

可以，wrapper 是一层一层包裹的。

然后，其实是 ModifiedLayoutNode 负责 measure：

```kotlin
internal class ModifiedLayoutNode(
    wrapped: LayoutNodeWrapper,
    modifier: LayoutModifier
) : DelegatingLayoutNodeWrapper<LayoutModifier>(wrapped, modifier) {

   override fun measure(constraints: Constraints): Placeable = performingMeasure(constraints) {
      with(modifier) {
         measureResult = measureScope.measure(wrapped, constraints)
         this@ModifiedLayoutNode
      }
   }

}
```

`measureScope.measure(wrapped, constraints)` 方法是 modifier 添加的。特定的 LayoutModifier 为 measureScope 添加了特定的扩展。

比如 PaddingModifier：

```kotlin
private class PaddingModifier(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val rtlAware: Boolean,
    inspectorInfo: InspectorInfo.() -> Unit
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
   
   init {
      require(
         (start.value >= 0f || start == Dp.Unspecified) &&
                 (top.value >= 0f || top == Dp.Unspecified) &&
                 (end.value >= 0f || end == Dp.Unspecified) &&
                 (bottom.value >= 0f || bottom == Dp.Unspecified)
      ) {
         "Padding must be non-negative"
      }
   }

   override fun MeasureScope.measure(
      measurable: Measurable,
      constraints: Constraints
   ): MeasureResult {

      val horizontal = start.roundToPx() + end.roundToPx()//水平 padding
      val vertical = top.roundToPx() + bottom.roundToPx()//垂直 padding

      //先做一次测量
      val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

      //再加上 padding
      val width = constraints.constrainWidth(placeable.width + horizontal)
      val height = constraints.constrainHeight(placeable.height + vertical)

      return layout(width, height) {
         if (rtlAware) {
            placeable.placeRelative(start.roundToPx(), top.roundToPx())
         } else {
            placeable.place(start.roundToPx(), top.roundToPx())
         }
      }
   }
   
}
```

Modifier 的 Padding 的逻辑是：先测量右边的，再回来计算左边的。

```kotlin
Modifier
    .padding(40.dp) //（2）确定了 160dp 后，再加上 40dp 的 padding，确定最终的大小。
    .size(160.dp) //（1）先进行一次测量，确定要 160dp
```

如果空间不够用呢（加上下面 modifier 用于修饰一个作为根元素的 Box）？

```kotlin
Modifier
   // （2）确定了 size 后，再加上 40dp 的 padding，确定最终的大小。
   .padding(40.dp)
    //（1）先进行一次测量，虽然要 800dp，但是会有父控件规范的最大 size，而且 val placeable = measurable.measure(constraints.offset(-horizontal, -vertical)) 中可以到，用于测量的 size 是剪掉了 padding 的。
    // 因此，最终确定的大小为，屏幕的大小减去 padding，即允许的最大的 size 减去要求的  padding，其余的全给 size。
    .size(800.dp)
    .background(Color.Black)
```

如果调用两次 size 呢？

```kotlin
 Box(
      Modifier
           //（3）最终确定大小为 160 + 40
          .padding(40.dp)
           //（2）又测量一次，改成了 160
          .size(160.dp)
           //（1）测量一次，确定为 80
          .size(80.dp)
          .background(Color.Blue)
  ) {

  }

Box(
   Modifier
      //（3）最终确定大小为 80 + 40
      .padding(40.dp)
      //（2）又测量一次，改成了 80
      .size(80.dp)
      //（1）测量一次，确定为 160
      .size(160.dp)
      .background(Color.Blue)
) {

}
```

对于 size 要求的大小，如果空间不够，则 Compose 会对其进行调整，还有一个 requiredSize，强制要求 size。
