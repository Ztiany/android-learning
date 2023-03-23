package me.ztiany.compose.realistic

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList

@Composable
fun RealisticScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}


private const val REALISTIC_PAGE = "realistic_page"
private const val REALISTIC_INTERNAL_PAGE = "realistic_internal_page"

private val entrances = linkedMapOf<String, @Composable () -> Unit>(
    "GoogleBaseLayoutCodeLab" to { GoogleBaseLayoutCodeLab() },
)

fun NavController.navigateToRealistic() {
    this.navigate(REALISTIC_PAGE)
}

fun NavGraphBuilder.realisticScreen(navController: NavHostController) {
    navigation(startDestination = REALISTIC_INTERNAL_PAGE, route = REALISTIC_PAGE) {
        composable(REALISTIC_INTERNAL_PAGE) {
            RealisticScreen(navController)
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                entrance.value()
            }
        }
    }
}