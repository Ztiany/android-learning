# Compose 学习

## 1 Compose 介绍

**声明式 UI**：理解什么是声明式 UI，主要只需要通过声明来构建 UI，杜绝命令式的 UI
交互，一切以数据为驱动，参考 [声明式 UI？Android 官方怒推的 Jetpack Compose 到底是什么](https://rengwuxian.com/jetpack-compose-3/)，、。

**独立于平台**：

1. Compose 独立于平台，不依赖于 Android 环境【暴露给开发者的 API 不依赖于 Android 环境，但是底层还是会使用到 Android 的相关 API，比如 Canvas】。
2. Compose 的完全重新设计的。
3. 正是因为 Compose 的独立于平台的特性，AndroidStudio 内置的预览功能非常强大，因为不需要依赖于 Android 的 API。

## 2 初步上手

### 图片

1. 支持位图：ImageBitmap
2. 支持矢量图：ImageVector

也可以提供要给 Painter 对象（类比 Drawable），使用 `painterResource()` 可以快速创建 Painter 对象。

**第三方库**：accompanist 起初由 ChrisBanes 开发，后面过继给 google，这个库提供了很多辅助功能，比如图片加载（其实就是包装一些现有的库 Glide、Coil 等）。

```groovy
implementation "com.google.accompanist:accompanist-coil:0.15.0"
```

具体的使用方式，参考 <https://coil-kt.github.io/coil/compose/>。

Compose 里面，Image 也显示纯色。

### 布局

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
Modifier.clickable {}.padding(8.dp)//这里 padding 区域响应点击。
Modifier.padding(8.dp).clickable {}//这里 padding 区域不响应点击。
```

**上面时候用 Modifier**：Modifier 用于做同样的属性设置，而控件特有的特性应该是用其他方式去设置。

**声明式 UI 的特点**：拿不到控件对象，比如 Text 是一个方法，没有返回值，你拿不到表示文本的那个对象，也就不可能拿这个对象去做样式修改，这就是声明式 UI，一切通过声明的方式来设置。

### Compose 组件分组

[compose](https://developer.android.com/jetpack/androidx/releases/compose?hl=zh-cn) 由 androidx 中的 6
个 Maven 组 ID 构成。每个组都包含一套特定用途的功能，并各有专属的版本说明。

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
3. `material` 的两个扩展组件 `material-icons-core`, `material-icons-extended` 提供了一些 MD
   的常用组件，其中 `material-icons-extended` 是可选的。

## 3 Compose 底层原理

```kotlin
class RengWuXianActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          Text("Hello")
        }
    }

}
```

`setContent` 函数是 ComponentActivity 的 扩展函数：

```kotlin
public fun ComponentActivity.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit
) {

    //尝试找到 ComposeView。
    val existingComposeView = window.decorView
        .findViewById<ViewGroup>(android.R.id.content)
        .getChildAt(0) as? ComposeView

    //找到了就基于整个 ComposeView 构建 UI
    if (existingComposeView != null) with(existingComposeView) {
        setParentCompositionContext(parent)
        setContent(content)
    }
    //找不到就为开发者创建一个 ComposeView，然后基于创建的 ComposeView 构建 UI。
    else ComposeView(this).apply {
        // Set content and parent **before** setContentView
        // to have ComposeView create the composition on attach
        setParentCompositionContext(parent)
        setContent(content)

        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        setOwners()

        //设置 Activity 的布局。
        setContentView(this, DefaultActivityContentLayoutParams)
    }
}
```

ComposeView 的 setContent 方法：

```kotlin
fun setContent(content: @Composable () -> Unit) {
    shouldCreateCompositionOnAttachedToWindow = true
    this.content.value = content
    //如果在 onCreate 方法中调用，则 isAttachedToWindow = false。
    if (isAttachedToWindow) {
        createComposition()
    }
}
```

ComposeView 的 onAttachedToWindow：

```kotlin
override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    previousAttachedWindowToken = windowToken

    if (shouldCreateCompositionOnAttachedToWindow) {
        ensureCompositionCreated()
    }
}

@Suppress("DEPRECATION") // Still using ViewGroup.setContent for now
private fun ensureCompositionCreated() {
    if (composition == null) {
        try {
            creatingComposition = true
            //创建 Composition 对象
            composition = setContent(resolveParentCompositionContext()) {
                Content()
            }
        } finally {
            creatingComposition = false
        }
    }
}
```

找 CompositionContext 的逻辑

```kotlin
private fun resolveParentCompositionContext() = parentContext
    ?: findViewTreeCompositionContext()?.also { cachedViewTreeCompositionContext = it }
    ?: cachedViewTreeCompositionContext
    ?: windowRecomposer.also { cachedViewTreeCompositionContext = it }

//默认情况下返回 null
fun View.findViewTreeCompositionContext(): CompositionContext? {
    var found: CompositionContext? = compositionContext
    if (found != null) return found
    var parent: ViewParent? = parent
    while (found == null && parent is View) {
        found = parent.compositionContext
        parent = parent.getParent()
    }
    return found
}

//默认会走 WindowRecomposerPolicy.createAndInstallWindowRecomposer(rootView)
internal val View.windowRecomposer: Recomposer
    get() {
        check(isAttachedToWindow) {
            "Cannot locate windowRecomposer; View $this is not attached to a window"
        }
        val rootView = contentChild
        return when (val rootParentRef = rootView.compositionContext) {
            null -> WindowRecomposerPolicy.createAndInstallWindowRecomposer(rootView)
            is Recomposer -> rootParentRef
            else -> error("root viewTreeParentCompositionContext is not a Recomposer")
        }
    }

//最终在中找到了 Recomposer 的创建逻辑（Recomposer 继承了 CompositionContext）
@OptIn(DelicateCoroutinesApi::class)
internal fun createAndInstallWindowRecomposer(rootView: View): Recomposer {
    val newRecomposer = factory.get().createRecomposer(rootView)
    rootView.compositionContext = newRecomposer

    val unsetJob = GlobalScope.launch(
        rootView.handler.asCoroutineDispatcher("windowRecomposer cleanup").immediate
    ) {
        try {
            newRecomposer.join()
        } finally {
            val viewTagRecomposer = rootView.compositionContext
            if (viewTagRecomposer === newRecomposer) {
                rootView.compositionContext = null
            }
        }
    }

    rootView.addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                unsetJob.cancel()
            }
        }
    )
    return newRecomposer
}

//val newRecomposer = factory.get().createRecomposer(rootView) 的实现
object WindowRecomposerPolicy {

    private val factory = AtomicReference<WindowRecomposerFactory>(
        WindowRecomposerFactory.LifecycleAware
    )
}

@InternalComposeUiApi
fun interface WindowRecomposerFactory {
    fun createRecomposer(windowRootView: View): Recomposer

    companion object {
        val LifecycleAware: WindowRecomposerFactory = WindowRecomposerFactory { rootView ->
            rootView.createLifecycleAwareViewTreeRecomposer()
        }
    }
}
```

createLifecycleAwareViewTreeRecomposer 方法的实现：

```kotlin
private fun View.createLifecycleAwareViewTreeRecomposer(): Recomposer {
    val currentThreadContext = AndroidUiDispatcher.CurrentThread
    val pausableClock = currentThreadContext[MonotonicFrameClock]?.let {
        PausableMonotonicFrameClock(it).apply { pause() }
    }
    val contextWithClock = currentThreadContext + (pausableClock ?: EmptyCoroutineContext)

    //核心：创建 Recomposer
    val recomposer = Recomposer(contextWithClock)

    val runRecomposeScope = CoroutineScope(contextWithClock)
    val viewTreeLifecycleOwner = checkNotNull(ViewTreeLifecycleOwner.get(this)) {
        "ViewTreeLifecycleOwner not found from $this"
    }
    // Removing the view holding the ViewTreeRecomposer means we may never be reattached again.
    // Since this factory function is used to create a new recomposer for each invocation and
    // doesn't reuse a single instance like other factories might, shut it down whenever it
    // becomes detached. This can easily happen as part of setting a new content view.
    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) {
                removeOnAttachStateChangeListener(this)
                recomposer.cancel()
            }
        }
    )
    viewTreeLifecycleOwner.lifecycle.addObserver(
        object : LifecycleEventObserver {
            override fun onStateChanged(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
                val self = this
                @Suppress("NON_EXHAUSTIVE_WHEN")
                when (event) {
                    Lifecycle.Event.ON_CREATE ->
                        // Undispatched launch since we've configured this scope
                        // to be on the UI thread
                        runRecomposeScope.launch(start = CoroutineStart.UNDISPATCHED) {
                            try {
                                //核心：用于页面刷新和数据刷新的逻辑【不包含】。
                                recomposer.runRecomposeAndApplyChanges()
                            } finally {
                                // If runRecomposeAndApplyChanges returns or this coroutine is
                                // cancelled it means we no longer care about this lifecycle.
                                // Clean up the dangling references tied to this observer.
                                lifecycleOwner.lifecycle.removeObserver(self)
                            }
                        }
                    Lifecycle.Event.ON_START -> pausableClock?.resume()
                    Lifecycle.Event.ON_STOP -> pausableClock?.pause()
                    Lifecycle.Event.ON_DESTROY -> {
                        recomposer.cancel()
                    }
                }
            }
        }
    )
    return recomposer
}
```

再回到 `resolveParentCompositionContext()` 方法，已经直到 windowRecomposer 的过程，windowRecomposer 是一个 Recomposer 类型，被创建后会被赋值给  `rootView.compositionContext` 的属性

再回到 ensureCompositionCreated，在 ensureCompositionCreated 方法中会调用 ViewGroup 的 setContent 方法，content 参数包裹了最初的 content。：

```kotlin
internal fun ViewGroup.setContent(
    parent: CompositionContext,
    content: @Composable () -> Unit
): Composition {
   
    //Snapshot 即快照，用于记录页面改动（SlotTable 用于记录页面结构）
    //GlobalSnapshotManager.ensureStarted() 会注册监听器，当界面发生变动，就会应用到页面上。
    GlobalSnapshotManager.ensureStarted()
   
    /*
    ComposeUI 的结构：
    
        ComposeView
                |
            AndroidComposeView（真正干活的）
     */
    val composeView =
        if (childCount > 0) {
            getChildAt(0) as? AndroidComposeView
        } else {
            removeAllViews(); null
        } ?: AndroidComposeView(context).also { addView(it.view, DefaultLayoutParams) }
   
    return doSetContent(composeView, parent, content)
}
```

doSetContent 的逻辑

```kotlin
private fun doSetContent(
    owner: AndroidComposeView,
    parent: CompositionContext,
    content: @Composable () -> Unit
): Composition {
   
    if (inspectionWanted(owner)) {
        owner.setTag(
            R.id.inspection_slot_table_set,
            Collections.newSetFromMap(WeakHashMap<CompositionData, Boolean>())
        )
        enableDebugInspectorInfo()
    }
   
   //创建Composition
    val original = Composition(UiApplier(owner.root), parent)
   
   //创建 WrappedComposition
    val wrapped = owner.view.getTag(R.id.wrapped_composition_tag)
        as? WrappedComposition
        ?: WrappedComposition(owner, original).also {
            owner.view.setTag(R.id.wrapped_composition_tag, it)
        }
   
    //又一个 setContent
    wrapped.setContent(content)
    return wrapped
}
```

wrapped.setContent(content) 的逻辑

```kotlin
@OptIn(InternalComposeApi::class)
    override fun setContent(content: @Composable () -> Unit) {
        owner.setOnViewTreeOwnersAvailable {
            if (!disposed) {
                val lifecycle = it.lifecycleOwner.lifecycle
                lastContent = content
                if (addedToLifecycle == null) {
                    addedToLifecycle = lifecycle
                    // this will call ON_CREATE synchronously if we already created
                    lifecycle.addObserver(this)
                } else if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    
                    //核心，调用 original 的 setContent，其实就是一个 Composition。
                    original.setContent {
                        @Suppress("UNCHECKED_CAST")
                        val inspectionTable =
                            owner.getTag(R.id.inspection_slot_table_set) as?
                                MutableSet<CompositionData>
                                ?: (owner.parent as? View)?.getTag(R.id.inspection_slot_table_set)
                                    as? MutableSet<CompositionData>
                        if (inspectionTable != null) {
                            @OptIn(InternalComposeApi::class)
                            inspectionTable.add(currentComposer.compositionData)
                            currentComposer.collectParameterInformation()
                        }

                        LaunchedEffect(owner) { owner.keyboardVisibilityEventLoop() }
                        LaunchedEffect(owner) { owner.boundsUpdatesEventLoop() }

                        CompositionLocalProvider(LocalInspectionTables provides inspectionTable) {
                            ProvideAndroidCompositionLocals(owner, content)
                        }
                    }
                   
                }
            }
        }
    }
```

Composition 是这样创建的：

```kotlin
fun Composition(
    applier: Applier<*>,
    parent: CompositionContext
): Composition =
    CompositionImpl(
        parent,
        applier
    )
```

所以调用的是 CompositionImpl 的 setContent

```kotlin
 override fun setContent(content: @Composable () -> Unit) {
     check(!disposed) { "The composition is disposed" }
     this.composable = content
     parent.composeInitial(this, composable)
 }
```

parent 其实就是传入的 WindowRecomposer（实现类为 CompositionContextImpl），它的 composeInitial 方法如下：

```kotlin
internal override fun composeInitial(
        composition: ControlledComposition,
        content: @Composable () -> Unit
    ) {
        val composerWasComposing = composition.isComposing
        composing(composition, null) {
            composition.composeContent(content)
        }
        // TODO(b/143755743)
        if (!composerWasComposing) {
            Snapshot.notifyObjectsInitialized()
        }
        composition.applyChanges()

        synchronized(stateLock) {
            if (_state.value > State.ShuttingDown) {
                if (composition !in knownCompositions) {
                    knownCompositions += composition
                }
            }
        }

        if (!composerWasComposing) {
            // Ensure that any state objects created during applyChanges are seen as changed
            // if modified after this call.
            Snapshot.notifyObjectsInitialized()
        }
    }
```

然后是 composing 方法：

```kotlin
 private inline fun <T> composing(
     composition: ControlledComposition,
     modifiedValues: IdentityArraySet<Any>?,
     block: () -> T
 ): T {
     val snapshot = Snapshot.takeMutableSnapshot(
         readObserverOf(composition), writeObserverOf(composition, modifiedValues)
     )
     try {
         //核心：调用 block
         return snapshot.enter(block)
     } finally {
         applyAndCheck(snapshot)
     }
 }
```

block 就是调用 `composition.composeContent(content)`

```kotlin
 override fun composeContent(content: @Composable () -> Unit) {
        // TODO: This should raise a signal to any currently running recompose calls
        // to halt and return
        synchronized(lock) {
            drainPendingModificationsForCompositionLocked()
           //调用 composeContent，其实就是 CompositionContextImpl 的 composeContent
            composer.composeContent(takeInvalidations(), content)
        }
    }
```

然后就是

```kotlin
internal fun composeContent(
   invalidationsRequested: IdentityArrayMap<RecomposeScopeImpl, IdentityArraySet<Any>?>,
   content: @Composable () -> Unit
) {
   runtimeCheck(changes.isEmpty()) { "Expected applyChanges() to have been called" }
   doCompose(invalidationsRequested, content)
}


 private fun doCompose(
     invalidationsRequested: IdentityArrayMap<RecomposeScopeImpl, IdentityArraySet<Any>?>,
     content: (@Composable () -> Unit)?
 ) {
     runtimeCheck(!isComposing) { "Reentrant composition is not supported" }
     trace("Compose:recompose") {
         snapshot = currentSnapshot()
         invalidationsRequested.forEach { scope, set ->
             val location = scope.anchor?.location ?: return
             invalidations.add(Invalidation(scope, location, set))
         }
         invalidations.sortBy { it.location }
         nodeIndex = 0
         var complete = false
         isComposing = true
         try {
             startRoot()
             // Ignore reads of derivedStatOf recalculations
             observeDerivedStateRecalculations(
                 start = {
                     childrenComposing++
                 },
                 done = {
                     childrenComposing--
                 },
             ) {
                 if (content != null) {
                     startGroup(invocationKey, invocation)

                     //核心代码
                     invokeComposable(this, content)
                     endGroup()
                 } else {
                     skipCurrentGroup()
                 }
             }
             endRoot()
             complete = true
         } finally {
             isComposing = false
             invalidations.clear()
             providerUpdates.clear()
             if (!complete) abortRoot()
         }
     }
 }
```

invokeComposable 的代码

```kotlin
internal fun invokeComposable(composer: Composer, composable: @Composable () -> Unit) {
    // 这里为什么 composable 可以被转为 Function2，composable 是没有参数的才对。
    // 原理是：Compose 的编译插件会为 composable 加上一些参数。
    @Suppress("UNCHECKED_CAST")
    val realFn = composable as Function2<Composer, Int, Unit>
    realFn(composer, 1)
}
```

再回到开始：

```kotlin
class RengWuXianActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         Text(text = "")
      }
   }
   
}
```

追踪 Text 的创建，会找到 Layout 函数：

```kotlin
@Suppress("ComposableLambdaParameterPosition")
@Composable inline fun Layout(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    measurePolicy: MeasurePolicy
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val viewConfiguration = LocalViewConfiguration.current
   
    ReusableComposeNode<ComposeUiNode, Applier<Any>>(
        factory = ComposeUiNode.Constructor,
        update = {
            set(measurePolicy, ComposeUiNode.SetMeasurePolicy)
            set(density, ComposeUiNode.SetDensity)
            set(layoutDirection, ComposeUiNode.SetLayoutDirection)
            set(viewConfiguration, ComposeUiNode.SetViewConfiguration)
        },
        skippableUpdate = materializerOf(modifier),
        content = content
    )
   
}
```

ReusableComposeNode

```kotlin
inline fun <T, reified E : Applier<*>> ReusableComposeNode(
    noinline factory: () -> T,
    update: @DisallowComposableCalls Updater<T>.() -> Unit,
    noinline skippableUpdate: @Composable SkippableUpdater<T>.() -> Unit,
    content: @Composable () -> Unit
) {
    if (currentComposer.applier !is E) invalidApplier()
    currentComposer.startReusableNode()
   
    if (currentComposer.inserting) {
        currentComposer.createNode(factory)
    } else {
        currentComposer.useNode()
    }
    currentComposer.disableReusing()
    Updater<T>(currentComposer).update()
    currentComposer.enableReusing()
    SkippableUpdater<T>(currentComposer).skippableUpdate()
    currentComposer.startReplaceableGroup(0x7ab4aae9)
    content()
    currentComposer.endReplaceableGroup()
    currentComposer.endNode()
}
```

Android Compose UI 的结构

```kotlin
decorView
    LinearLayout
        android.R.id.content
            ComposeView
                AndroidComposeView
                    LayoutNode
                        LayoutNode——【Text】
                        LayoutNode——【Column】
                        LayoutNode——【Image】
```

找到 createNode 的逻辑：

```kotlin
 @Suppress("UNUSED")
override fun <T> createNode(factory: () -> T) {
   validateNodeExpected()
   runtimeCheck(inserting) { "createNode() can only be called when inserting" }
   val insertIndex = nodeIndexStack.peek()
   val groupAnchor = writer.anchor(writer.parent)
   groupNodeCount++
   recordFixup { applier, slots, _ ->
      @Suppress("UNCHECKED_CAST")
      val node = factory()
      slots.updateNode(groupAnchor, node)
      @Suppress("UNCHECKED_CAST") val nodeApplier = applier as Applier<T>
      //插入
      nodeApplier.insertTopDown(insertIndex, node)
      applier.down(node)
   }
   recordInsertUpFixup { applier, slots, _ ->
      @Suppress("UNCHECKED_CAST")
      val nodeToInsert = slots.node(groupAnchor)
      applier.up()
      @Suppress("UNCHECKED_CAST") val nodeApplier = applier as Applier<Any?>
      nodeApplier.insertBottomUp(insertIndex, nodeToInsert)
   }
}
```

UiApplier

```kotlin
internal class UiApplier(
    root: LayoutNode
) : AbstractApplier<LayoutNode>(root) {

    override fun insertTopDown(index: Int, instance: LayoutNode) {
        // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
        // duplicate notification when the child nodes enter the tree.
    }

    override fun insertBottomUp(index: Int, instance: LayoutNode) {
        current.insertAt(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        current.removeAt(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.move(from, to, count)
    }

    override fun onClear() {
        root.removeAll()
    }

    override fun onEndChanges() {
        super.onEndChanges()
        (root.owner as? AndroidComposeView)?.clearInvalidObservations()
    }
}
```

里面的 current 就是一个 LayoutNode。

- [ ] 进一步分析

## 4 自定义 Composable

**Composable 注解**：

1. Composable 是一个标识符，用于告诉编译器插件这个方法用于生成界面，编译器插件会对这些方法做一些修改。
2. 加了 Composable 的方法必须在别的加了 Composable 方法中调用。
3. 不用于构建界面的函数就没必要加 Composable 注解了，没有作用，反而增加编译器的负担。

**自定义 Composable 相当于什么**？

相当于 xml 布局文件 + 自定义 View/ViewGroup。既可以有布局又可以有逻辑，即具有 xml 简单直观的特点，又可以加上逻辑。

**自定义 Composable 的应用场景**？【总结：需要 `xml/自定义View/Group` 的场景】

1. 简单的布局拆分。【相当于 xml】
2. 逻辑逻辑定制的界面。【相当于自定义 View/ViewGroup】
3. 需要定制绘制、布局、触摸反馈的界面。【基于 Modifier】
