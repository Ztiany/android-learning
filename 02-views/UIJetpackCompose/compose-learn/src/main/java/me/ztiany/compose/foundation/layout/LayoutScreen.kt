package me.ztiany.compose.foundation.layout

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList

@Composable
fun LayoutsScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return listOf(
        Entrance("Row/Colum 的使用") { navController.navigateToArtistCard() },
        Entrance("Constrains-1") { navController.navigateToConstrains1() },
        Entrance("Constrains-2") { navController.navigateToConstrains2() },
        Entrance("Constrains-3") { navController.navigateToConstrains3() },
        Entrance("Scaffold-1") { navController.navigateToScaffold() },
        Entrance("Surface") { navController.navigateToSurface() },
        Entrance("BottomSheet") { navController.navigateToBottomSheet() },
    )
}