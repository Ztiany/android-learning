package me.ztiany.compose.rwx.version2.chapter5_modifier

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val ROUTE_NAME = "chapter05_modifier"
private const val START_PAGE = "Chapter 5: Modifier"

@Composable
private fun ModifierScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "S501_WhatIsModifier" to { S501_WhatIsModifier() },
    "S503_ComposedModifier" to { S503_ComposedModifier() },
    "S504_LayoutModifier" to { S504_LayoutModifier() },
    "S505_LayoutModifier" to { S505_LayoutModifier() },
    "S506_DrawModifier" to { S506_DrawModifier() },
    "S507_DrawModifier" to { S507_DrawModifier() },
    "S509_PointerInputModifier" to { S509_PointerInputModifier() },
    "S510_ParentDataModifier" to { S510_ParentDataModifier() },
    "S511_ParentDataModifier" to { S511_ParentDataModifier() },
    "S512_ParentDataModifier" to { S512_ParentDataModifier() },
    "S513_SemanticsModifier" to { S513_SemanticsModifier() },
    "S514_AddBefore_AddAfter" to { S514_AddBefore_AddAfter() },
    "S515_OnRemeasuredModifier" to { S515_OnRemeasuredModifier() },
    "S516_OnPlacedModifier" to { S516_OnPlacedModifier() },
    "S517_LookaheadOnPlacedModifier" to { S517_LookaheadOnPlacedModifier() },
    "S518_OnGloballyPositionModifier" to { S518_OnGloballyPositionModifier() },
    "S519_ModifierLocal" to { S519_ModifierLocal() },
)

fun NavController.navigateToChapter5() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.chapter5Screen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        ModifierScreen(navController)
    }
}