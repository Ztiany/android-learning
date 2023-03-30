package me.ztiany.compose.facilities.widget

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

private class KeyComposable(
    val name: String,
    val screen: @Composable (NavBackStackEntry) -> Unit,
)


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

@NavigationDsl
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

    fun sections(buildBlack: EntranceListBuilder.() -> Unit) = EntranceListBuilder().apply {
        buildBlack()
        this@EntranceNavigationBuilder.entranceListBuilder = this
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

@NavigationDsl
class EntranceListBuilder {

    private val _items = mutableListOf<Any>()
    val items: List<Any>
        get() = _items

    fun newSection(header: String, entrances: EntranceListScope.() -> Unit) {
        _items.add(header)
        object : EntranceListScope {
            override fun entrance(name: String, screen: @Composable (NavBackStackEntry) -> Unit) {
                this@EntranceListBuilder._items.add(KeyComposable(name, screen))
            }

            override fun String.to(screen: @Composable (NavBackStackEntry) -> Unit) {
                this@EntranceListBuilder._items.add(KeyComposable(this, screen))
            }
        }.entrances()
    }

}

@NavigationDsl
interface EntranceListScope {
    fun entrance(name: String, screen: @Composable (NavBackStackEntry) -> Unit)
    infix fun String.to(screen: @Composable (NavBackStackEntry) -> Unit)
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
            items.filterIsInstance<KeyComposable>().forEach {
                composable(it.name) { navEntry -> it.screen(navEntry) }
            }
        }
    }

    private fun buildEntrances(navController: NavController): List<Item> {
        return items.map {
            when (it) {
                is String -> Header(it)

                is KeyComposable -> Entrance(it.name) {
                    navController.navigate(it.name)
                }

                else -> throw IllegalStateException("internal error.")
            }
        }
    }

}

@DslMarker
annotation class NavigationDsl