package me.ztiany.compose.sample

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation
import me.ztiany.compose.sample.googlebasic.GoogleBaseLayoutCodeLab

private const val ROUTE_NAME = "samples_route"
private const val START_PAGE = "Samples"

@Composable
private fun SampleScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Google Basic Layout CodeLab" to { GoogleBaseLayoutCodeLab() },
)

fun NavController.navigateToSamples() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.samplesScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        SampleScreen(navController)
    }
}