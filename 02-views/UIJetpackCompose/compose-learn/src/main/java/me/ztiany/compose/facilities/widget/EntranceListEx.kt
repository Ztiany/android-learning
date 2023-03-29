package me.ztiany.compose.facilities.widget

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

typealias ComposableLambda = @Composable (NavBackStackEntry) -> Unit

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
): List<Item> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}

class EntranceNavigationBuilder {

    private var routeName: String = ""
    private var startDestination: String = ""
    private var startScreen: @Composable (List<Item>, NavBackStackEntry) -> Unit = { _, _ -> }
    private var entranceListBuilder: EntranceListBuilder? = null

    fun route(routeName: String) {
        this.routeName = routeName
    }

    fun startDestination(startDestination: String, screen: @Composable (List<Item>, NavBackStackEntry) -> Unit) {
        this.startDestination = startDestination
        this.startScreen = screen
    }

    fun entranceList(buildBlack: EntranceListBuilder.() -> Unit) = EntranceListBuilder().apply {
        buildBlack()
        entranceListBuilder = this
    }

    fun toEntranceNavigationMaker(): EntranceNavigationMaker {
        if (routeName.isEmpty() || startDestination.isEmpty()) {
            throw IllegalStateException("routeName and startDestination cannot be empty.")
        }
        return EntranceNavigationMaker(
            routeName, startDestination, startScreen, entranceListBuilder?.items ?: emptyList()
        )
    }

}

class EntranceListBuilder {

    private val _items = mutableListOf<Any>()
    val items: List<Any>
        get() = _items

    fun header(name: String) {
        _items.add(name)
    }

    fun entrance(name: String, screen: @Composable (NavBackStackEntry) -> Unit) {
        _items.add(name to screen)
    }

    infix fun String.to(screen: @Composable (NavBackStackEntry) -> Unit) {
        _items.add(Pair(this, screen))
    }

}

fun buildEntranceNavigation(buildBlock: EntranceNavigationBuilder.() -> Unit) = EntranceNavigationBuilder().apply(buildBlock)

class EntranceNavigationMaker(
    private var routeName: String,
    private var startDestination: String,
    private var screen: @Composable (List<Item>, NavBackStackEntry) -> Unit,
    private val items: List<Any>
) {

    fun buildNavigation(navHostController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(route = routeName, startDestination = startDestination) {
            composable(startDestination) {
                screen(buildEntrances(navHostController), it)
            }
            items.filterIsInstance<Pair<*, *>>().forEach {
                composable(it.first as String) { navEntry ->
                    @Suppress("UNCHECKED_CAST")
                    (it.second as ComposableLambda)(navEntry)
                }
            }
        }
    }

    fun buildEntrances(navController: NavController): List<Item> {
        return items.map {
            when (it) {
                is String -> Header(it)

                is Pair<*, *> -> Entrance(it.first as String) {
                    navController.navigate(it.first as String)
                }

                else -> throw IllegalStateException("internal error.")
            }
        }
    }

}