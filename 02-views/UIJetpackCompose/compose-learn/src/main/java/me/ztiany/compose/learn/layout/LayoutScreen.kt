package me.ztiany.compose.learn.layout

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation

private const val LAYOUT_PAGE = "layout_page"
private const val LAYOUT_INTERNAL_PAGE = "layout_internal_page"

@Composable
private fun LayoutsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

fun NavController.navigateToLayouts() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.layoutScreen(navController: NavHostController) {
    buildNavigation(LAYOUT_PAGE, entrances, LAYOUT_INTERNAL_PAGE) {
        LayoutsScreen(navController)
    }
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Row & Colum" to { ArtistCard() },
    "Constrains-1" to { ConstraintLayoutDemo() },
    "Constrains-2" to { ConstraintLayoutDemo() },
    "Constrains-2" to { QuotesDemo() },
    "Constrains-3" to { UserPortraitDemo() },
    "Google Starter Tutorial CodeLab" to { GoogleStarterTutorialScreen() },
    "Google First ComposeAPP CodeLab" to { GoogleFirstComposeAppCodeLabScreen() },
)