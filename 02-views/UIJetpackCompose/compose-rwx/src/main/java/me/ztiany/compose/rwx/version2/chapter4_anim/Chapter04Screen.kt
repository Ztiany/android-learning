package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val LAYOUT_PAGE = "chapter04_animation"
private const val LAYOUT_INTERNAL_PAGE = "chapter04_animation_screen"

@Composable
private fun AnimationScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    "s401_AnimateXxxAsState" to { S401_AnimateXxxAsState() },
    "S401_Animatable" to { S402_Animatable() },
    "S403_AnimationSpec_TweenSpec" to { S403_AnimationSpec_TweenSpec() },
    "S404_AnimationSpec_SnapSpec" to { S404_AnimationSpec_SnapSpec() },
    "S405_AnimationSpec_KeyframesSpec" to { S405_AnimationSpec_KeyframesSpec() },
    "S406_AnimationSpec_SpringSpec" to { S406_AnimationSpec_SpringSpec() },
    "S407_AnimationSpec_RepeatableSpec" to { S407_AnimationSpec_RepeatableSpec() },
    "S408_AnimationSpec_InfiniteRepeatableSpec" to { S408_AnimationSpec_InfiniteRepeatableSpec() },
)

fun NavController.navigateToChapter4() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.chapter4Screen(navController: NavHostController) {
    buildNavigation(LAYOUT_PAGE, entrances, LAYOUT_INTERNAL_PAGE) {
        AnimationScreen(navController)
    }
}