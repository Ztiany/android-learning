package me.ztiany.compose.learn.gesture

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation
import me.ztiany.compose.learn.gesture.highlevel.ClickableViews
import me.ztiany.compose.learn.gesture.highlevel.DraggableViews
import me.ztiany.compose.learn.gesture.highlevel.NestedScrollViews
import me.ztiany.compose.learn.gesture.highlevel.ScrollableViews
import me.ztiany.compose.learn.gesture.highlevel.SelectableScreen
import me.ztiany.compose.learn.gesture.highlevel.SwipeableViews
import me.ztiany.compose.learn.gesture.highlevel.TransformableViews
import me.ztiany.compose.learn.gesture.pointerinput.AwaitForEachGestureExample
import me.ztiany.compose.learn.gesture.pointerinput.DetectDragGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.DetectTapGesturesViews
import me.ztiany.compose.learn.gesture.pointerinput.ForEachGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.TransformGestureViews
import me.ztiany.compose.learn.gesture.pointerinput.awaiteach.AwaitDragOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.awaiteach.AwaitFirstDownViews
import me.ztiany.compose.learn.gesture.pointerinput.awaiteach.AwaitPointerEventViews
import me.ztiany.compose.learn.gesture.pointerinput.awaiteach.AwaitTouchSlopOrCancellationViews
import me.ztiany.compose.learn.gesture.pointerinput.awaiteach.DragViews
import me.ztiany.compose.learn.gesture.practice.IOSSpringExample
import me.ztiany.compose.learn.gesture.practice.PullToRefreshExample

private const val ROUTE_NAME = "gesture_page"
private const val START_PAGE = "Gesture"

private val NavigationMaker = buildEntranceNavigation {
    route(ROUTE_NAME)

    startDestination(START_PAGE, true) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        //===================================
        // 高级 API
        //===================================
        newSection("高级 API") {
            //点击、双击、长按
            "Clickable and CombinedClickable" asTitleTo { ClickableViews() }
            //开关、选择
            "Selectable and Toggleable" asTitleTo { SelectableScreen() }
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
        // PointerInputScope 扩展
        //===================================
        newSection("PointerInputScope 扩展") {
            "DetectTapGestures" asTitleTo { DetectTapGesturesViews() }
            "DetectDragGestures" asTitleTo { DetectDragGesturesViews() }
            "TransformGesture" asTitleTo { TransformGestureViews() }
            "ForEachGesture (Core) (Deprecated)" asTitleTo { ForEachGestureViews() }
            "AwaitEachGesture (Core) (取代 ForEachGesture)" asTitleTo { AwaitForEachGestureExample() }
        }

        //===================================
        //底层 API：AwaitEachGesture 应用
        //===================================
        newSection("AwaitEachGesture 应用") {
            "AwaitEachGesture-AwaitPointerEvent" asTitleTo { AwaitPointerEventViews() }
            "AwaitEachGesture-AwaitFirstDown" asTitleTo { AwaitFirstDownViews() }
            "AwaitEachGesture-Drag" asTitleTo { DragViews() }
            "AwaitEachGesture-AwaitDragOrCancellation" asTitleTo { AwaitDragOrCancellationViews() }
            "AwaitEachGesture-AwaitTouchSlopOrCancellation" asTitleTo { AwaitTouchSlopOrCancellationViews() }
        }

        //===================================
        // Practice
        //===================================
        newSection("Practice") {
            "iOS Spring" asTitleTo { IOSSpringExample() }
            "Custom PullToRefresh" asTitleTo { PullToRefreshExample() }
        }
    }

}.toEntranceNavigationMaker()

fun NavController.navigateToGesture() {
    this.navigate(ROUTE_NAME)
}

/**
 * 手势处理学习，参考：
 *
 * 1. [官方指南：Gestures Handling](https://developer.android.com/develop/ui/compose/touch-input/pointer-input)
 * 2. [官方文档：Gestures Package](https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary)
 * 3. [Jetpack Compose Museum: Gesture](https://jetpackcompose.cn/docs/category/%E6%89%8B%E5%8A%BFgesture)
 */
fun NavGraphBuilder.gestureScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}