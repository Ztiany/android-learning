package me.ztiany.compose.learn.state

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.Entrance
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.SimpleScaffold
import me.ztiany.compose.facility.widget.buildNavigation
import me.ztiany.compose.learn.state.google.GoogleStateInComposeCodeLab
import me.ztiany.compose.learn.state.statetest.HelloStateScreen

private const val ROUTE_NAME = "state_managing_page"
private const val START_PAGE = "state_managing_internal_page"

@Composable
private fun StateManagingScreen(navController: NavHostController) {
    SimpleScaffold(title = "State Managing") {
        EntranceList(entranceList = buildEntrances(navController))
    }
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "HelloStateScreen" to { HelloStateScreen() },
    "Google StateInCompose CodeLab" to { GoogleStateInComposeCodeLab() }
)

fun NavController.navigateToStateManaging() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.stateManagingScreen(navController: NavHostController) {
    buildNavigation(false, ROUTE_NAME, entrances, START_PAGE) {
        StateManagingScreen(navController)
    }
}