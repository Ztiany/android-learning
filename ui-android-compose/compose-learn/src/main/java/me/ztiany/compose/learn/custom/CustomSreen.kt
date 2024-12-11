package me.ztiany.compose.learn.custom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation

private const val ROUTE_NAME = "custom_page"
private const val START_PAGE = "Custom Draw"

private val NavigationMaker = buildEntranceNavigation {
    route(ROUTE_NAME)

    startDestination(START_PAGE, true) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        newSection("自定义绘制") {
            //drawBehind 修饰符 / drawWithContent 修饰符
            "画红点" asTitleTo { DrawRedDotScreen() }
            //Canvas 组件
            "Canvas 组件" asTitleTo { DrawLoadingProgressBar() }
            //drawWithCache 修饰符
            "DrawWithCache" asTitleTo { DrawWithCacheDemo() }
            //GraphicLayer 的使用
            "GraphicLayerScreen" to { GraphicLayerScreen() }
            //实践 + 原始 Canvas API
            "WaveLoading" asTitleTo { WaveLoadingDemo() }
        }

        newSection("自定义测量与布局") {
            //layout 修饰符
            "Layout 修饰符" asTitleTo { FirstBaselineToTopExample() }
            //Layout 组件
            "Layout 组件" asTitleTo { SimpleColumnExample() }
            //IntrinsicSize / SubcomposeLayout
            "IntrinsicSize and SubcomposeLayout" asTitleTo { TwoTextsScreen() }
        }
    }
}.toEntranceNavigationMaker()

fun NavController.navigateToCustomDrawAndLayout() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.customScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}