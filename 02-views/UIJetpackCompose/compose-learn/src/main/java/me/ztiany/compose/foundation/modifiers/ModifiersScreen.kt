package me.ztiany.compose.foundation.modifiers

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.facilities.widget.buildEntranceNavigation

/*
1. https://stackoverflow.com/questions/64989659/when-do-you-need-modifier-composed
 */
@Composable
fun ModifiersScreen() {

}


private const val MODIFIER_PAGE = "modifier_page"
private const val MODIFIER_INTERNAL_PAGE = "modifier_internal_page"

private val NavigationMaker = buildEntranceNavigation {
    route(MODIFIER_PAGE)

    startDestination(MODIFIER_INTERNAL_PAGE) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        newSection("Modifier 的执行顺序") {
            "Center by Modifier" to { CenterByModifier() }
            "Order of Modifier" to { OrderOfModifier() }
        }
    }
}.toEntranceNavigationMaker()

fun NavController.navigateToModifier() {
    this.navigate(MODIFIER_PAGE)
}

fun NavGraphBuilder.modifierScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}