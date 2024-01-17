package me.ztiany.compose.rwx.version2.chapter6_sideeffect

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val ROUTE_NAME = "chapter06_side_effect"
private const val START_PAGE = "Chapter 6: Side Effect"

@Composable
private fun SideEffectScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "S601_SideEffect" to { S601_SideEffect() }
)

fun NavController.navigateToChapter6() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.chapter6Screen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        SideEffectScreen(navController)
    }
}