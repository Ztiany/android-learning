package me.ztiany.compose.facilities.widget

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.buildNavigation(
    routeName: String,
    startDestination: String,
    entrances: Map<String, @Composable (NavBackStackEntry) -> Unit>,
    screen: @Composable (NavBackStackEntry) -> Unit
) {
    navigation(startDestination = startDestination, route = routeName) {
        composable(startDestination) {
            screen(it)
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                entrance.value(it)
            }
        }
    }
}

fun buildEntrances(
    entrances: Map<String, @Composable (NavBackStackEntry) -> Unit>,
    navController: NavController
): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}