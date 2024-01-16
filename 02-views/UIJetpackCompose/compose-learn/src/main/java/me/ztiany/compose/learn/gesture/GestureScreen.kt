package me.ztiany.compose.learn.gesture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation
import me.ztiany.compose.learn.gesture.pointerinput.DetectDragGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.DetectTapGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.ForEachGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.TransformGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitDragOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitFirstDownViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitPointerEventViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitTouchSlopOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.DragViews

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

    sections {
        //===================================
        //高级 API
        //===================================
        newSection("高级 API"){
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
        }

        //===================================
        //PointerInputScope 扩展
        //===================================
        newSection("PointerInputScope 扩展"){
            "DetectTapGestures" to { DetectTapGesturesViews() }
            "DetectDragGestures" to { DetectDragGesturesViews() }
            "TransformGesture" to { TransformGestureViews() }
            "ForEachGesture" to { ForEachGestureViews() }
        }

        //===================================
        //底层 API：forEachGesture 应用
        //===================================
        newSection("forEachGesture 应用"){
            "ForEach-AwaitPointerEvent" to { AwaitPointerEventViews() }
            "ForEach-AwaitFirstDown" to { AwaitFirstDownViews() }
            "ForEach-Drag" to { DragViews() }
            "ForEach-AwaitDragOrCancellation" to { AwaitDragOrCancellationViews() }
            "ForEach-AwaitTouchSlopOrCancellation" to { AwaitTouchSlopOrCancellationViews() }
        }
    }

}.toEntranceNavigationMaker()

fun NavController.navigateToGesture() {
    this.navigate(GESTURE_PAGE)
}

fun NavGraphBuilder.gestureScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}