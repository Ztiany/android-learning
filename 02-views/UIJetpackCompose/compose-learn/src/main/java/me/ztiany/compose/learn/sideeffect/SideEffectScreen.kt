package me.ztiany.compose.learn.sideeffect

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntranceNavigation

private const val SIDE_EFFECT_PAGE = "side_effect_page"
private const val SIDE_EFFECT_INTERNAL_PAGE = "side_effect_internal_page"

private val NavigationMaker = buildEntranceNavigation {
    route(SIDE_EFFECT_PAGE)

    startDestination(SIDE_EFFECT_INTERNAL_PAGE, true) { list, _ ->
        EntranceList(entranceList = list)
    }

    sections {
        newSection("Side Effect API 示例") {
            "SideEffectExample" asTitleTo { SideEffectExample() }
            "DisposableEffectExample" asTitleTo { DisposableEffectExample() }
            "DerivedStateOfExample" asTitleTo { DerivedStateOfExample() }
            "ProduceStateExample" asTitleTo { ProduceStateExample() }
            "RememberUpdatedStateExample" asTitleTo { RememberUpdatedStateExample() }
            "LaunchedEffectExample" to { LaunchedEffectExample() }
            "RememberCoroutineScopeExample" to { RememberCoroutineScopeExample() }
        }

    }
}.toEntranceNavigationMaker()

fun NavController.navigateToSideEffect() {
    this.navigate(SIDE_EFFECT_PAGE)
}

fun NavGraphBuilder.sideEffectScreen(navController: NavHostController) {
    NavigationMaker.buildNavigation(navController, this)
}