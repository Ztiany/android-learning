# Compose 学习

## Compose 介绍

**声明式 UI**：理解什么是声明式 UI，参考 [声明式 UI？Android 官方怒推的 Jetpack Compose 到底是什么](https://rengwuxian.com/jetpack-compose-3/)。

**独立于平台**：

1. Compose 独立于平台，不依赖于 Android 环境【暴露给开发者的 API 不依赖于 Android 环境，但是底层还是会使用到 Android 的相关 API，比如 Canvas】。
2. Compose 的完全重新设计的。
3. 正是因为 Compose 的独立于平台的特性，AndroidStudio 内置的预览功能非常强大，因为不需要依赖于 Android 的 API。

## 图片

1. 支持位图：ImageBitmap
2. 支持矢量图：ImageVector

也可以提供要给 Painter 对象（类比 Drawable），使用 `painterResource()` 可以快速创建 Painter 对象。

**第三方库**：accompanist 起初由 ChrisBanes 开发，后面过继给 google，这个库提供了很多辅助功能，比如图片加载（其实就是包装一些现有的库 Glide、Coil 等）。

```groovy
implementation "com.google.accompanist:accompanist-coil:0.15.0"
```

具体的使用方式，参考 <https://coil-kt.github.io/coil/compose/>。

Compose 里面，Image 也显示纯色。

## 布局

- Column 纵向
- Row 横向

其实 Column 和 Row 最终使用的是通用的布局逻辑，在 API 层面区分开来，是为了方便开发者。

调整布局：

1. 使用 Modifier 来调整。 
2. Compose 里面只有 padding 没有 margin【padding 和 margin 的区分为是否绘制背景色】。
3. 但是 Compose 里面，Modifier 对函数调用顺序是敏感的，比如：

```kotlin
Modifier.padding(8.dp).background(Color.Black)//这里 padding 影响背景。
Modifier.background(Color.Black).padding(8.dp)//这里 padding 不影响背景。
Modifier.clickable{}.padding(8.dp)//这里 padding 区域响应点击。
Modifier.padding(8.dp).clickable{}//这里 padding 区域不响应点击。
```

**上面时候用 Modifier**：Modifier 用于做同样的属性设置，而控件特有的特性应该是用其他方式去设置。

**声明式 UI 的特点**：拿不到控件对象，比如 Text 是一个方法，没有返回值，你拿不到表示文本的那个对象，也就不可能拿这个对象去做样式修改，这就是声明式 UI，一切通过声明的方式来设置。

## Compose 组件分组

[compose](https://developer.android.com/jetpack/androidx/releases/compose?hl=zh-cn) 由 androidx 中的 6 个 Maven 组 ID 构成。每个组都包含一套特定用途的功能，并各有专属的版本说明。

为什么要进行分组呢？ 为了方便扩展，进行分层设计，这对于持续的更新来说，是非常方便的。

分层说明（从下到上）：

1. 底层组件：`compose.compiler`，这个组件不需要明确配置，是 Compose 在编译期的插件。
2. `compose.runtime` 是 Compose 最底层的设计，比如通用的数据结构、状态转换机制等。
3. `compose.ui` 是 Compose 最基础的 UI 支持，比如测量布局绘制、触摸反馈等。
4. `compose.animation` 是 Compose 对动画的支持。
5. `compose.foundation` 提供了常用的具体的控件。
6. `compose.material` 是 google 提供的基于 Compose 的 Material 风格化组件。

扩展组件：

1. 比如 `ui` 组件依赖了 `ui.util`，所以不需要明确依赖 `ui.util`。
2. `ui.tooling` 提供了预览支持，其依赖于 `ui` 组件。
3. `material` 的两个扩展组件 `material-icons-core`, `material-icons-extended` 提供了一些 MD 的常用组件，其中 `material-icons-extended` 是可选的。