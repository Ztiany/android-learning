package me.ztiany.compose.facility.widget

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation

fun buildEntranceNavigation(buildBlock: EntranceNavigationBuilder.() -> Unit) = EntranceNavigationBuilder().apply(buildBlock)

private class KeyComposable(
    val name: String,
    val screen: @Composable (NavBackStackEntry) -> Unit,
)

@DslMarker
annotation class NavigationDsl

@NavigationDsl
class EntranceNavigationBuilder {

    private var routeName: String = ""
    private var startDestination: String = ""
    private var startScreen: @Composable (List<Item>, NavBackStackEntry) -> Unit = { _, _ -> }
    private var entranceListBuilder: EntranceListBuilder? = null

    fun route(routeName: String) {
        this.routeName = routeName
    }

    fun startDestination(startDestination: String, asTitle: Boolean = false, screen: @Composable (List<Item>, NavBackStackEntry) -> Unit) {
        this.startDestination = startDestination
        this.startScreen = if (asTitle) {
            { items, navBackStackEntry ->
                SimpleScaffold(title = startDestination) {
                    screen(items, navBackStackEntry)
                }
            }
        } else {
            screen
        }
    }

    fun sections(buildBlock: EntranceListBuilder.() -> Unit) = EntranceListBuilder().apply {
        buildBlock()
        this@EntranceNavigationBuilder.entranceListBuilder = this
    }

    fun toEntranceNavigationMaker(): EntranceNavigationMaker {
        if (routeName.isEmpty() || startDestination.isEmpty()) {
            throw IllegalStateException("routeName and startDestination cannot be empty.")
        }
        return EntranceNavigationMaker(
            routeName,
            entranceListBuilder?.items ?: emptyList(),
            startDestination,
            startScreen
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
            override fun entrance(name: String, asTitle: Boolean, screen: @Composable (NavBackStackEntry) -> Unit) {
                val value: @Composable (NavBackStackEntry) -> Unit = if (asTitle) {
                    { navBackStackEntry ->
                        SimpleScaffold(title = name) {
                            screen(navBackStackEntry)
                        }
                    }
                } else {
                    screen
                }

                this@EntranceListBuilder._items.add(KeyComposable(name, value))
            }

            override fun String.to(screen: @Composable (NavBackStackEntry) -> Unit) {
                this@EntranceListBuilder._items.add(KeyComposable(this, screen))
            }

            override fun String.asTitleTo(screen: @Composable (NavBackStackEntry) -> Unit) {
                this@EntranceListBuilder._items.add(KeyComposable(this) {
                    SimpleScaffold(title = this, content = { screen(it) })
                })
            }
        }.entrances()
    }

}

@NavigationDsl
interface EntranceListScope {
    fun entrance(name: String, asTitle: Boolean = false, screen: @Composable (NavBackStackEntry) -> Unit)

    infix fun String.to(screen: @Composable (NavBackStackEntry) -> Unit)

    infix fun String.asTitleTo(screen: @Composable (NavBackStackEntry) -> Unit)
}

class EntranceNavigationMaker(
    private var routeName: String,
    private val items: List<Any>,
    private var startDestination: String,
    private var startScreen: @Composable (List<Item>, NavBackStackEntry) -> Unit
) {

    fun buildNavigation(navHostController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(route = routeName, startDestination = startDestination) {
            composableWithScaleAnimation(startDestination) {
                startScreen(buildEntrances(navHostController), it)
            }
            items.filterIsInstance<KeyComposable>().forEach {
                composableWithScaleAnimation(it.name) { navEntry -> it.screen(navEntry) }
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