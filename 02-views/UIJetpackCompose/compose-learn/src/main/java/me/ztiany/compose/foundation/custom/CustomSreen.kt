package me.ztiany.compose.foundation.custom

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildEntrances
import me.ztiany.compose.facilities.widget.buildNavigation

private const val CUSTOM_PAGE = "custom_page"
private const val CUSTOM_INTERNAL_PAGE = "custom_internal_page"

@Composable
private fun CustomScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    //drawBehind 修饰符 / drawWithContent 修饰符
    "画红点" to { DrawRedDotScreen() },
    //Canvas 组件
    "Canvas 组件" to { DrawLoadingProgressBar() },
    //drawWithCache 修饰符
    "DrawWithCache" to { DrawWithCacheDemo() },
    //layout 修饰符
    "Layout 修饰符" to { FirstBaselineToTopExample() },
    //Layout 组件
    "Layout 组件" to { SimpleColumnExample() },
    //IntrinsicSize / SubcomposeLayout
    "IntrinsicSize" to { TwoTextsScreen() },
    //实践 + 原始 Canvas API
    "WaveLoading" to { WaveLoadingDemo() }
)

fun NavController.navigateToCustomDrawAndLayout() {
    this.navigate(CUSTOM_PAGE)
}

fun NavGraphBuilder.customScreen(navController: NavHostController) {
    buildNavigation(CUSTOM_PAGE, CUSTOM_INTERNAL_PAGE, entrances) {
        CustomScreen(navController = navController)
    }
}