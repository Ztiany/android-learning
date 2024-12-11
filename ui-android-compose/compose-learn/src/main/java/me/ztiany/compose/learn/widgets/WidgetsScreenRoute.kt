package me.ztiany.compose.learn.widgets

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import me.ztiany.compose.facility.widget.SimpleScaffold

private const val WIDGETS_PAGE = "widgets_page"

fun NavController.navigateToWidgets() {
    this.navigate(WIDGETS_PAGE)
}

fun NavGraphBuilder.widgetScreen(navController: NavHostController) {
    composable(route = WIDGETS_PAGE) {
        SimpleScaffold(title = "Widgets") {
            WidgetsScreen()
        }
    }
}