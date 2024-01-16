package me.ztiany.compose.learn.gesture

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "NestedScrollExample"

/*
我们在开发时，可能经常需要通过处理嵌套滑动来解决手势冲突问题。简单地说，就是协调父 View 与子 View 的交互逻辑关系，从而实现各类手势需求。在 View 体系中，可以通过重写
ViewGroup 的 onInterceptTouchEvent 来定制处理。这么做可能比较麻烦，一般都会直接使用 NestedScrollView 来实现。在 Compose 中官方为我们实现了 nestedScroll 修饰符，可以
专门用来处理嵌套滑动手势，这也为父组件劫持消费子组件所触发的滑动手势提供了可能。

在使用 nestedScroll 修饰符时，需要传入一个必选参数 connection 和一个可选参数 dispatcher。

            • connection：包含了嵌套滑动手势处理的核心逻辑，通过内部回调可以在子布局获得滑动事件前，预先消费掉部分或全部手势偏移量，当然也可以获取子布局消费后剩下的手势偏移量。
            • dispatcher：包含用于父布局的 NestedScrollConnection，可以使用包含的 dispatch* 系列方法动态控制组件完成滑动。
 */
@Composable
fun NestedScrollViews() {
    SmartSwipeRefresh(onRefresh = {
        Log.d(TAG, "onRefresh")
    }) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            for (i in 0..100) {
                Box(
                    Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.Blue)
                ) {
                    Text(text = i.toString(), color = Color.White, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

class SmartSwipeRefreshState {
    private val indicatorOffsetAnimatable = Animatable(0.dp, Dp.VectorConverter)

    val indicatorOffset get() = indicatorOffsetAnimatable.value

    private val _indicatorOffsetFlow = MutableStateFlow(0f)
    val indicatorOffsetFlow: Flow<Float> get() = _indicatorOffsetFlow

    val isSwipeInProgress by derivedStateOf { indicatorOffset != 0.dp }

    var isRefreshing: Boolean by mutableStateOf(false)

    fun updateOffsetDelta(value: Float) {
        _indicatorOffsetFlow.value = value
    }

    suspend fun snapToOffset(value: Dp) {
        indicatorOffsetAnimatable.snapTo(value)
    }

    suspend fun animateToOffset(value: Dp) {
        indicatorOffsetAnimatable.animateTo(value, tween(1000))
    }
}

private class SmartSwipeRefreshNestedScrollConnection(
    val state: SmartSwipeRefreshState, val height: Dp
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        Log.d(TAG, "onPreScroll available.y=${available.y}")
        return if (source == NestedScrollSource.Drag && available.y < 0) {
            state.updateOffsetDelta(available.y)
            if (state.isSwipeInProgress) Offset(x = 0f, y = available.y) else Offset.Zero
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset, available: Offset, source: NestedScrollSource
    ): Offset {
        Log.d(TAG, "onPostScroll available.y=${available.y}")
        return if (source == NestedScrollSource.Drag && available.y > 0) {
            state.updateOffsetDelta(available.y)
            Offset(x = 0f, y = available.y)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        Log.d(TAG, "onPreFling available.y=${available.y}")
        if (state.indicatorOffset > height / 2) {
            Log.d(TAG, "onPreFling 1 state.isRefreshing=${state.isRefreshing}")
            if (state.isRefreshing) {
                //取消往回滑动的动画
                state.isRefreshing = false
            }
            state.animateToOffset(height)
            state.isRefreshing = true
        } else {
            Log.d(TAG, "onPreFling 2")
            state.animateToOffset(0.dp)
        }
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        Log.d(TAG, "onPostFling available.y=${available.y}")
        return super.onPostFling(consumed, available)
    }

}

@Composable
private fun SubcomposeSmartSwipeRefresh(
    indicator: @Composable () -> Unit,
    //注意：这里主要是为了得到 header 的高
    content: @Composable (Dp) -> Unit
) {
    SubcomposeLayout { constraints: Constraints ->

        val indicatorPlaceable = subcompose("indicator", indicator).first().measure(constraints)

        val contentPlaceable = subcompose("content") {
            content(indicatorPlaceable.height.toDp())
        }.map {
            it.measure(constraints)
        }.first()

        Log.d(TAG, "dp: ${indicatorPlaceable.height.toDp()}")

        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.placeRelative(0, 0)
        }
    }
}

@Composable
fun SmartSwipeRefresh(
    onRefresh: suspend () -> Unit,
    state: SmartSwipeRefreshState = remember { SmartSwipeRefreshState() },
    loadingIndicator: @Composable () -> Unit = { CircularProgressIndicator() },
    content: @Composable () -> Unit
) {
    SubcomposeSmartSwipeRefresh(indicator = loadingIndicator) { height ->

        val smartSwipeRefreshNestedScrollConnection = remember(state, height) {
            SmartSwipeRefreshNestedScrollConnection(state, height)
        }

        Box(
            Modifier.nestedScroll(smartSwipeRefreshNestedScrollConnection), contentAlignment = Alignment.TopCenter
        ) {

            Box(Modifier.offset(y = -height + state.indicatorOffset)) {
                loadingIndicator()
            }

            Box(Modifier.offset(y = state.indicatorOffset)) {
                content()
            }

        }

        val density = LocalDensity.current

        LaunchedEffect(Unit) {
            state.indicatorOffsetFlow.collect {
                val currentOffset = with(density) { state.indicatorOffset + it.toDp() }
                state.snapToOffset(currentOffset.coerceAtLeast(0.dp).coerceAtMost(height))
            }
        }

        LaunchedEffect(state.isRefreshing) {
            Log.d(TAG, "LaunchedEffect-Back-1")
            if (state.isRefreshing) {
                Log.d(TAG, "LaunchedEffect-Back-2")
                onRefresh()
                state.animateToOffset(0.dp)
                state.isRefreshing = false
            }
        }
    }
}
