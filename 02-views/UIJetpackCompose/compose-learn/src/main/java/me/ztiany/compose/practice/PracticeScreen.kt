package me.ztiany.compose.practice

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation
import me.ztiany.compose.practice.paging3.FlexiblePaging3Screen
import me.ztiany.compose.practice.paging3.Paging3Screen
import me.ztiany.compose.practice.refreshstate.RefreshStateScreen
import me.ztiany.compose.practice.simplestate.SimpleStateScreen

private const val ROUTE_NAME = "practice_route"
private const val START_PAGE = "Practice"

@Composable
private fun PracticeScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances.map { it.key }, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "MultiState Page" to { _ -> SimpleStateScreen() },
    "Refresh with MultiState Page" to { _ -> RefreshStateScreen() },
    "Paging3 Page(1)" to { _ -> Paging3Screen() },
    "Paging3 Page(2)" to { _ -> FlexiblePaging3Screen() },
)

fun NavController.navigateToPractice() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.practiceScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        PracticeScreen(navController)
    }
}