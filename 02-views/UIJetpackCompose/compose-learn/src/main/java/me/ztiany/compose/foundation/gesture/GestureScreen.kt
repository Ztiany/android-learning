package me.ztiany.compose.foundation.gesture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildEntranceNavigation
import me.ztiany.compose.foundation.gesture.pointerinput.DetectDragGesturesViews
import me.ztiany.compose.foundation.gesture.pointerinput.DetectTapGesturesViews
import me.ztiany.compose.foundation.gesture.pointerinput.ForEachGestureViews
import me.ztiany.compose.foundation.gesture.pointerinput.TransformGestureViews
import me.ztiany.compose.foundation.gesture.pointerinput.foreach.*

private const val GESTURE_PAGE = "gesture_page"
private const val GESTURE_INTERNAL_PAGE = "gesture_internal_page"

/**
 * 手势处理学习，参考：
 *
 * 1. https://jetpackcompose.cn/docs/category/%E6%89%8B%E5%8A%BFgesture
 */
private val NavigationMaker = buildEntranceNavigation {
    route(GESTURE_PAGE)

    startDestination(GESTURE_INTERNAL_PAGE) { list, _ ->
        EntranceList(entranceList = list)
    }

    entranceList {
        //===================================
        //高级 API
        //===================================
        header("高级 API")
        //点击、双击、长按
        "Clickable" to { ClickableViews() }
        //水平、垂直方向拖动
        "Draggable" to { DraggableViews() }
        //水平、垂直方向拖动
        "Swipeable" to { SwipeableViews() }
        //多指触控
        "Transformable" to { TransformableViews() }
        //滚动
        "ScrollableViews" to { ScrollableViews() }
        //嵌套滚动
        "NestedScrollViews" to { NestedScrollViews() }

        //===================================
        //PointerInputScope 扩展
        //===================================
        header("PointerInputScope 扩展")
        "DetectTapGestures" to { DetectTapGesturesViews() }
        "DetectDragGestures" to { DetectDragGesturesViews() }
        "TransformGesture" to { TransformGestureViews() }
        "ForEachGesture" to { ForEachGestureViews() }

        //===================================
        //底层 API：forEachGesture 应用
        //===================================
        header("forEachGesture 应用")
        "ForEach-AwaitPointerEvent" to { AwaitPointerEventViews() }
        "ForEach-AwaitFirstDown" to { AwaitFirstDownViews() }
        "ForEach-Drag" to { DragViews() }
        "ForEach-AwaitDragOrCancellation" to { AwaitDragOrCancellationViews() }
        "ForEach-AwaitTouchSlopOrCancellation" to { AwaitTouchSlopOrCancellationViews() }
    }

}.toEntranceNavigationMaker()

fun NavController.navigateToGesture() {
    this.navigate(GESTURE_PAGE)
}

fun NavGraphBuilder.gestureScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}