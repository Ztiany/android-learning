package me.ztiany.compose.learn.modifier

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation

private const val ROUTE_NAME = "modifier_page"
private const val START_PAGE = "Modifier"

private val NavigationMaker = buildEntranceNavigation {
    route(ROUTE_NAME)

    startDestination(START_PAGE, true) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        newSection("Modifier 的执行顺序") {
            "Center by Modifier" to { CenterByModifierExample() }
            "Order of Modifier" to { OrderOfModifierExample() }
            "Background Size" to { BackgroundSizeSample() }
        }
        newSection("UI 效果相关的 Modifier") {
            "Marquee" asTitleTo { MarqueeExample() }
            "MagnifierExample" asTitleTo { MagnifierExample() }
        }
    }
}.toEntranceNavigationMaker()

fun NavController.navigateToModifier() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.modifierScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}