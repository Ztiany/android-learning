# Compose 学习

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

- [ ] todo：进一步分析

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

## 5 状态订阅于自动更新

参考 Lesson05

## 6 状态机制的背后

参考 Lesson06

## 7 Modifier 深度解析

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

## 8 动画？

参考 Lesson08

## 9 能自定义 View 么？

参考 Lesson09

## 10 与传统 View 交互

参考 Lesson10
