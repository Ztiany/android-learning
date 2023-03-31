package me.ztiany.compose.foundation.custom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildEntranceNavigation

private const val CUSTOM_PAGE = "custom_page"
private const val CUSTOM_INTERNAL_PAGE = "custom_internal_page"

private val NavigationMaker = buildEntranceNavigation {
    route(CUSTOM_PAGE)

    startDestination(CUSTOM_INTERNAL_PAGE) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        newSection("自定义绘制"){
            //drawBehind 修饰符 / drawWithContent 修饰符
            "画红点" to { DrawRedDotScreen() }
            //Canvas 组件
            "Canvas 组件" to { DrawLoadingProgressBar() }
            //drawWithCache 修饰符
            "DrawWithCache" to { DrawWithCacheDemo() }
            //GraphicLayer 的使用
            "GraphicLayerScreen" to { GraphicLayerScreen() }
            //实践 + 原始 Canvas API
            "WaveLoading" to { WaveLoadingDemo() }
        }

        newSection("自定义测量与布局"){
            //layout 修饰符
            "Layout 修饰符" to { FirstBaselineToTopExample() }
            //Layout 组件
            "Layout 组件" to { SimpleColumnExample() }
            //IntrinsicSize / SubcomposeLayout
            "IntrinsicSize and SubcomposeLayout" to { TwoTextsScreen() }
        }
    }
}.toEntranceNavigationMaker()

fun NavController.navigateToCustomDrawAndLayout() {
    this.navigate(CUSTOM_PAGE)
}

fun NavGraphBuilder.customScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}