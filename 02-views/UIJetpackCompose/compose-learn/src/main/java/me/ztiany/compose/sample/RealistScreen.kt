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

private const val REALISTIC_PAGE = "samples_page"
private const val REALISTIC_INTERNAL_PAGE = "samples_internal_page"

@Composable
private fun SampleScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Google Basic Layout CodeLab" to { GoogleBaseLayoutCodeLab() },
)

fun NavController.navigateToSamples() {
    this.navigate(REALISTIC_PAGE)
}

fun NavGraphBuilder.samplesScreen(navController: NavHostController) {
    buildNavigation(REALISTIC_PAGE, entrances, REALISTIC_INTERNAL_PAGE) {
        SampleScreen(navController)
    }
}