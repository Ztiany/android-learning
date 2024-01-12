package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val LAYOUT_PAGE = "chapter05_modifier"
private const val LAYOUT_INTERNAL_PAGE = "chapter05_modifier_screen"

@Composable
private fun AnimationScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "S501_WhatIsModifier" to { S501_WhatIsModifier() },
    "S503_ComposedModifier" to { S503_ComposedModifier() },
    "S504_LayoutModifier" to { S504_LayoutModifier() },
)

fun NavController.navigateToChapter5() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.chapter5Screen(navController: NavHostController) {
    buildNavigation(LAYOUT_PAGE, entrances, LAYOUT_INTERNAL_PAGE) {
        AnimationScreen(navController)
    }
}