package me.ztiany.compose.foundation.gesture

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildEntrances
import me.ztiany.compose.facilities.widget.buildNavigation

private const val GESTURE_PAGE = "gesture_page"
private const val GESTURE_INTERNAL_PAGE = "gesture_internal_page"

/**
 * 参考：
 *
 * 1. https://jetpackcompose.cn/docs/category/%E6%89%8B%E5%8A%BFgesture
 */
@Composable
private fun LayoutsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

fun NavController.navigateToGesture() {
    this.navigate(GESTURE_PAGE)
}

fun NavGraphBuilder.gestureScreen(navController: NavHostController) {
    buildNavigation(GESTURE_PAGE, GESTURE_INTERNAL_PAGE, entrances) {
        LayoutsScreen(navController)
    }
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    //===================================
    //高级 API
    //===================================
    //点击、双击、长按
    "Clickable" to { ClickableViews() },
    //水平、垂直方向拖动
    "Draggable" to { DraggableViews() },
    //水平、垂直方向拖动
    "Swipeable" to { SwipeableViews() },
    //多指触控
    "Transformable" to { TransformableViews() },
    //滚动
    "ScrollableViews" to { ScrollableViews() },
    //嵌套滚动
    "NestedScrollViews" to { NestedScrollViews() },
    //中层 API
    "DetectTapGestures" to { DetectTapGesturesViews() },
    "DetectDragGestures" to { DetectDragGesturesViews() },
    "TransformGesture" to { TransformGestureViews() },
    "ForEachGesture" to { ForEachGestureViews() },
    //底层 API
    "AwaitPointerEvent" to { AwaitPointerEventViews() },
    "AwaitFirstDown" to { AwaitFirstDownViews() },
    "Drag" to { DragViews() },
    "AwaitDragOrCancellation" to { AwaitDragOrCancellationViews() },
    "AwaitTouchSlopOrCancellation" to { AwaitTouchSlopOrCancellationViews() }
)
