package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val ROUTE_NAME = "chapter07_custom_layout"
private const val START_PAGE = "Chapter 7: Custom Layout"

@Composable
private fun SideEffectScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "S701_Draw" to { S701_Draw() },
    "S702_Layout" to { S702_Layout() },
    "S703_SubcomposeLayout" to { S703_SubcomposeLayout() },
    "S704_LookaheadLayout" to { S704_LookaheadLayout() },
    "S705_TouchHandling" to { S705_TouchHandling() },
    "S706_NestedScroll" to { S706_NestedScroll() },
    "S707_2DimensionScrollObservation" to { S707_2DimensionScrollObservation() },
    "S708_MultiFingers" to { S708_MultiFingers() },
    "S709_Underlying" to { S709_Underlying() },
)

fun NavController.navigateToChapter7() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.chapter7Screen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        SideEffectScreen(navController)
    }
}