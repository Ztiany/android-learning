package me.ztiany.compose.platform

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation

private const val ROUTE_NAME = "platform_route"
private const val START_PAGE = "Platform"

@Composable
private fun PlatformScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances.map { it.key }, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Dynamic Permission" to { _ -> DynamicPermissionScreen() },
)

fun NavController.navigateToPlatform() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.platformScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        PlatformScreen(navController)
    }
}