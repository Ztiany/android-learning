# 《Jetpack Compose：从上手到进阶再到高手》学习

## 2 用 Compose 写一个简单界面

### 什么是声明式 UI

**声明式 UI**：理解什么是声明式 UI，主要只需要通过声明来构建 UI，杜绝命令式的 UI  交互，一切以数据为驱动，参考 [声明式 UI？Android 官方怒推的 Jetpack Compose 到底是什么](https://rengwuxian.com/jetpack-compose-3/)，、。

### 独立于平台

**独立于平台**：

1. Compose 独立于平台，不依赖于 Android 环境【暴露给开发者的 API 不依赖于 Android 环境，但是底层还是会使用到 Android 的相关 API，比如 Canvas】。
2. Compose 的完全重新设计的。
3. 正是因为 Compose 的独立于平台的特性，AndroidStudio 内置的预览功能非常强大，因为不需要依赖于 Android 的 API。

在底层依然依赖于 Canvas，但是在 API 层绝对是独立于平台的，因此 AndroidStudio 也可以写一套 API 来解析并预览 Compose 写的界面。另外，基于 JetBrains 的支持，**Compose 也开始支持 Mac、Windows、Linux**。【利益关系：Compose 推广开来了，Kotlin 的使用者也就多了】

### 图片

首先是 Image 组件

1. 支持位图：ImageBitmap
2. 支持矢量图：ImageVector

另外也可以提供一个 Painter 对象（类比 Drawable），使用 `painterResource()` 可以快速创建 Painter 对象。

**第三方库**：[Accompanist](https://github.com/google/accompanist) 这个库提供了很多辅助功能，首先由 ChrisBanes，后续 ChrisBanes 离开 Google，但是将该项目过继给了 Google。Compose 的字面意思有“作曲”的意思，Accompanist 的意思是伴奏者，相对于 Compose 标准库中的代码，虽然 Accompanist 中的功能也很重要，但是没有那么稳定，可以认为其是新功能的孵化器。

对于网络图片的加载和显示，Accompanist 起初包装了 Glide、Coil 等常用的图片加载库，后续 Coil 官方提供了对 Compose 的支持，因此 Accompanist 中便移除了相关代码。开发者现在直接使用 Coil 即可。

```groovy
implementation "com.google.accompanist:accompanist-coil:0.15.0"
```

具体的使用方式，参考 <https://coil-kt.github.io/coil/compose/>。

### 传统 Layout 的 Compose 平替

- FrameLayout ---- Box
- LinearLayout ---- Colum/Row
- RelativeLayout ---- Box + Modifier 控制位置
- ConstraintLayout/MotionLayout ---- Box【简单布局】/ ConstraintLayout 的 Compose 版【复杂布局】
- ScrollView ---- 使用 Modifier.verticalScroll() 等 API
- RecyclerView ---- LazyColum
- ViewPager
- ...

### Modifier 的几个特性

#### 没有 Margin

传统 View 中：

- padding 表示内边距
- margin 表示外边距

Compose 里面只有 padding 没有 margin【padding 和 margin 的区分为是否绘制背景色】，但是 Compose 里面，Modifier 对函数调用顺序是敏感的，比如：

```kotlin
Modifier.padding(8.dp).background(Color.Black)//这里 padding 影响背景。
Modifier.background(Color.Black).padding(8.dp)//这里 padding 不影响背景。
Modifier.clickable {}.padding(8.dp)//这里 padding 区域响应点击。
Modifier.padding(8.dp).clickable {}//这里 padding 区域不响应点击。
```

除此之外，对 Modifier 相同 API 的多次调用，效果可能是叠加的。

#### 默认的 size

Compose 中，不强制要求写组件的 size，不写则默认行为类似于传统 View 的 wrap-content。

#### 什么时候用 Modifier

Modifier 用于做通用的属性设置（比如宽高、点击、背景色等），而控件特有的特性应该是用在控件函数的参数中去设置（比如 Text 的 fontSize）。

但是 Button 有一个 onClick 参数，不过这也不是搞特殊，本质上还是用的 Modifier，目的是为了方便开发者，Button 本就是用来做点击的。

#### 声明式 UI 的特点

拿不到控件对象，比如 Text 是一个方法，没有返回值，你拿不到表示文本的那个对象，也就不可能拿这个对象去做样式修改，这就是声明式 UI，一切通过声明的方式来设置。

### Compose 分包

[compose](https://developer.android.com/jetpack/androidx/releases/compose?hl=zh-cn) 由 androidx 中的 6
个 Maven 组 ID 构成。每个组都包含一套特定用途的功能，并各有专属的版本说明。

为什么要进行分组呢？ 为了方便扩展，进行分层设计，这对于持续的更新来说，是非常方便的。【另外一方面也是从传统 View 系统的无分组设计导致比较高的耦合性，学习到的经验教训】

分层说明（从下到上）：

1. 底层组件：`compose.compiler`，这个组件不需要明确配置，是 Compose 在编译期的插件。
2. `compose.runtime` 是 Compose 最底层的设计，比如通用的数据结构、状态转换机制等。
3. `compose.ui` 是 Compose 最基础的 UI 支持，比如基本组件、测量布局绘制、触摸反馈等。
4. `compose.animation` 是 Compose 对动画的支持。
5. `compose.foundation` 提供了常用的具体的控件，相对完整的 UI 体系【没有 foundation 也能使用 Compose，但是有了就更方便，因为 foundation 对很多常见功能都提供了实现，开发者也可以基于 foundation 来扩展，设计自己特定风格的系统元素】。
6. `compose.material` 是 google 提供的基于 Compose 的 Material 风格化组件。【是 Google 提供的一套带有特性设计风格的组件，开发者可以不使用 Material】
7. `compose.material3` 是 google 提供的基于 Compose 的 Material3 风格化组件。【是 Google 提供的一套带有特性设计风格的组件，开发者可以不使用 Material3】

扩展组件：

1. 比如 `ui` 组件依赖了 `ui.util`，所以不需要明确依赖 `ui.util`。
2. `ui-tooling` 提供了预览支持，其依赖于 `ui-tooling-preview` 组件，而 `ui-tooling-preview` 组件又依赖于 `ui` 组件。
3. `material` 的两个扩展组件 `material-icons-core`, `material-icons-extended` 提供了一些 MD 的常用组件，其中 `material-icons-extended` 是可选的。


## 3 Compose 动画

### 自定义 Composable

Compose 的编译器插件（Compiler Plugin）为开发者做了很多事情，编译器插件通过 `@Composable` 注解来识别需要处理的函数。 内置的 `invokeComposable` 方法是 Compose 的入口。

**Composable 注解**：

1. Composable 是一个标识符，用于告诉编译器插件这个方法用于生成界面，编译器插件会对这些方法做一些修改。
2. 加了 Composable 的方法必须在别的加了 Composable 方法中调用。
3. 不用于构建界面的函数就没必要加 Composable 注解了，没有作用，反而增加编译器的负担。

**自定义 Composable 相当于什么**？

所谓自定义 Composable 其实就是编写带有 `@Composable` 注解的函数来描述 UI。 

就相当于 xml 布局文件 + 自定义 View/ViewGroup。既可以有布局又可以有逻辑，即具有 xml 简单直观的特点，又可以加上逻辑。

**自定义 Composable 的应用场景**？【总结：需要 `xml/自定义View/Group` 的场景】

1. 简单的布局拆分。【相当于 xml】
2. 逻辑逻辑定制的界面。【相当于自定义 View/ViewGroup】
3. 需要定制绘制、布局、触摸反馈的界面。【基于 Modifier】

组合过程：拼凑出界面实际内容的过程。

整体的绘制流程：组合（重组）-->布局-->绘制。

代码过程：ComposeView-->AndroidComposeView-->LayoutNode-->LayoutNode...

术语：

- 过程：Compose/Composition
- 结果：Composition

### MutableState

在 Compose 中，UI 通过订阅 MutableState 类型的变量来实现自动感知数据变更并重绘 UI 的功能。MutableState 的创建方式如下，使用 `mutableStateOf("A")` 函数即可创建一个 MutableState。

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

...
