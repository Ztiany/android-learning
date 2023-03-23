package me.ztiany.compose.foundation.custom

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList

@Composable
fun CustomScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}


private val entrances = linkedMapOf<String, @Composable () -> Unit>(
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

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}

private const val CUSTOM_PAGE = "custom_page"

private const val CUSTOM_INTERNAL_PAGE = "custom_internal_page"

fun NavController.navigateToCustomDrawAndLayout() {
    this.navigate(CUSTOM_PAGE)
}

fun NavGraphBuilder.customScreen(navController: NavHostController) {
    navigation(startDestination = CUSTOM_INTERNAL_PAGE, route = CUSTOM_PAGE) {
        composable(CUSTOM_INTERNAL_PAGE) {
            CustomScreen(navController)
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                entrance.value()
            }
        }
    }
}