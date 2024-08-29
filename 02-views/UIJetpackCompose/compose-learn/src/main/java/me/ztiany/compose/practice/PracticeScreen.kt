package me.ztiany.compose.practice

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation
import me.ztiany.compose.practice.refreshstate.RefreshStateScreen

private const val ROUTE_NAME = "practice_route"
private const val START_PAGE = "Practice"

@Composable
private fun PracticeScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances.map { it.key }, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Refresh with MultiState Page" to { _ -> RefreshStateScreen() },
)

fun NavController.navigateToPractice() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.practiceScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        PracticeScreen(navController)
    }
}