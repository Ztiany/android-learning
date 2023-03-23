package me.ztiany.compose.foundation.layout

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList

@Composable
fun LayoutsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}


private const val LAYOUT_PAGE = "layout_page"
private const val LAYOUT_INTERNAL_PAGE = "layout_internal_page"


private val entrances = linkedMapOf<String, @Composable () -> Unit>(
    "Row & Colum" to { ArtistCard() },
    "Constrains-1" to { ConstraintLayoutDemo() },
    "Constrains-2" to { ConstraintLayoutDemo() },
    "Constrains-2" to { QuotesDemo() },
    "Constrains-3" to { UserPortraitDemo() },
    "SimpleLazyList" to { SimpleLazyListDemo() },
)

fun NavController.navigateToLayouts() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.layoutScreen(navController: NavHostController) {
    navigation(startDestination = LAYOUT_INTERNAL_PAGE, route = LAYOUT_PAGE) {
        composable(LAYOUT_INTERNAL_PAGE) {
            LayoutsScreen(navController)
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                entrance.value()
            }
        }
    }
}