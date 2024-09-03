package me.ztiany.compose.facility.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun buildEntrances(
    entrances: List<String>,
    navController: NavController,
): List<Item> {
    return entrances.map {
        Entrance(it) {
            navController.navigate(it)
        }
    }
}

fun NavGraphBuilder.buildNavigation(
    withScaffold: Boolean = false,
    routeName: String,
    entrances: Map<String, @Composable (NavBackStackEntry) -> Unit>,
    startDestination: String,
    startScreen: @Composable (NavBackStackEntry) -> Unit,
) {
    navigation(startDestination = startDestination, route = routeName) {
        composable(startDestination) {
            if (withScaffold) {
                SimpleScaffold(title = startDestination) {
                    startScreen(it)
                }
            } else {
                startScreen(it)
            }
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                if (withScaffold) {
                    SimpleScaffold(title = entrance.key) {
                        entrance.value(it)
                    }
                } else {
                    entrance.value(it)
                }
            }
        }
    }
}

sealed interface Item

data class Entrance(
    val name: String,
    val onNavigating: () -> Unit,
) : Item

data class Header(
    val name: String,
) : Item

@Composable
fun EntranceList(spanCount: Int = 1, entranceList: List<Item>) {
    LazyVerticalGrid(columns = GridCells.Fixed(spanCount),
        // content padding
        contentPadding = PaddingValues(
            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
        ),

        //items
        content = {
            items(entranceList) {
                //TODO: make the header always full span.
                if (it is Header) {
                    Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                } else if (it is Entrance) {
                    Button(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        onClick = {
                            it.onNavigating()
                        }) {
                        Text(text = it.name, fontSize = 12.sp)
                    }
                }
            }
        })
}