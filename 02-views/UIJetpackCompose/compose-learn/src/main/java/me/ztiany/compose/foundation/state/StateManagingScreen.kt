package me.ztiany.compose.foundation.state

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildNavigation

private const val STATE_MANAGING_PAGE = "state_managing_page"
private const val STATE_MANAGING_INTERNAL_PAGE = "state_managing_internal_page"

@Composable
private fun StateManagingScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Google StateInCompose CodeLab" to { GoogleStateInComposeCodeLab() }
)

fun NavController.navigateToStateManaging() {
    this.navigate(STATE_MANAGING_PAGE)
}

fun NavGraphBuilder.stateManagingScreen(navController: NavHostController) {
    buildNavigation(STATE_MANAGING_PAGE, STATE_MANAGING_INTERNAL_PAGE, entrances) {
        StateManagingScreen(navController)
    }
}