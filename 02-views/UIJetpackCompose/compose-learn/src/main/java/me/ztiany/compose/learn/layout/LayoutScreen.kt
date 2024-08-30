package me.ztiany.compose.learn.layout

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation

private const val ROUTE_NAME = "layout_route"
private const val START_PAGE = "Layouts"

@Composable
private fun LayoutsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

fun NavController.navigateToLayouts() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.layoutScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        LayoutsScreen(navController)
    }
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Default Measure Policy" to { DefaultMeasureDemo() },
    "Row & Colum" to { ArtistCard() },
    "Constrains-1" to { ConstraintLayoutDemo() },
    "Constrains-2" to { ConstraintLayoutDemo() },
    "Constrains-2" to { QuotesDemo() },
    "Constrains-3" to { UserPortraitDemo() },
    "Google Starter Tutorial CodeLab" to { GoogleStarterTutorialScreen() },
    "Google First ComposeAPP CodeLab" to { GoogleFirstComposeAppCodeLabScreen() },
)