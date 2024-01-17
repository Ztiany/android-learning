package me.ztiany.compose.learn.gesture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation
import me.ztiany.compose.learn.gesture.pointerinput.AwaitForEachGestureExample
import me.ztiany.compose.learn.gesture.pointerinput.DetectDragGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.DetectTapGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.ForEachGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.TransformGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitDragOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitFirstDownViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitPointerEventViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.AwaitTouchSlopOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.foreach.DragViews

private const val ROUTE_NAME = "gesture_page"
private const val START_PAGE = "Gesture"

/**
 * 手势处理学习，参考：
 *
 * 1. https://jetpackcompose.cn/docs/category/%E6%89%8B%E5%8A%BFgesture
 */
private val NavigationMaker = buildEntranceNavigation {
    route(ROUTE_NAME)

    startDestination(START_PAGE, true) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        //===================================
        //高级 API
        //===================================
        newSection("高级 API") {
            //点击、双击、长按
            "Clickable" asTitleTo { ClickableViews() }
            //水平、垂直方向拖动
            "Draggable" asTitleTo { DraggableViews() }
            //水平、垂直方向拖动
            "Swipeable" asTitleTo { SwipeableViews() }
            //多指触控
            "Transformable" asTitleTo { TransformableViews() }
            //滚动
            "ScrollableViews" asTitleTo { ScrollableViews() }
            //嵌套滚动
            "NestedScrollViews" asTitleTo { NestedScrollViews() }
        }

        //===================================
        //PointerInputScope 扩展
        //===================================
        newSection("PointerInputScope 扩展") {
            "DetectTapGestures" asTitleTo { DetectTapGesturesViews() }
            "DetectDragGestures" asTitleTo { DetectDragGesturesViews() }
            "TransformGesture" asTitleTo { TransformGestureViews() }
            "ForEachGesture（废弃）" asTitleTo { ForEachGestureViews() }
            "AwaitForEachGesture（取代 ForEachGesture）" asTitleTo { AwaitForEachGestureExample() }
        }

        //===================================
        //底层 API：forEachGesture 应用
        //===================================
        newSection("forEachGesture 应用") {
            "ForEach-AwaitPointerEvent" asTitleTo { AwaitPointerEventViews() }
            "ForEach-AwaitFirstDown" asTitleTo { AwaitFirstDownViews() }
            "ForEach-Drag" asTitleTo { DragViews() }
            "ForEach-AwaitDragOrCancellation" asTitleTo { AwaitDragOrCancellationViews() }
            "ForEach-AwaitTouchSlopOrCancellation" asTitleTo { AwaitTouchSlopOrCancellationViews() }
        }
    }

}.toEntranceNavigationMaker()

fun NavController.navigateToGesture() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.gestureScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}