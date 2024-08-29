package me.ztiany.compose.learn.dialog

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation

private const val ROUTE_NAME = "dialog_screen"
private const val START_PAGE = "Dialogs"

fun NavController.navigateToDialogs() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.dialogScreen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        DialogsScreen(navController)
    }
}

@Composable
private fun DialogsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "Basic Dialog" to { BasicDialogScreen() },
    "Popup" to { PopupScreen() },
)