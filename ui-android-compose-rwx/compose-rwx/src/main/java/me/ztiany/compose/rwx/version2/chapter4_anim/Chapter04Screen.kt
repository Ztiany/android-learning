package me.ztiany.compose.rwx.version2.chapter4_anim

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.buildEntrances
import me.ztiany.compose.rwx.facility.buildNavigation


private const val ROUTE_NAME = "chapter04_animation"
private const val START_PAGE = "Chapter 4: Animation"

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
    "S409_AnimationSpec_OtherSpecs" to { S409_AnimationSpec_OtherSpecs() },
    "S410_AnimateDecay" to { S410_AnimateDecay() },
    "411_ObserveFrame" to { S411_ObserveFrame() },
    "S412_Animation_Stop_Cancel" to { S412_Animation_Stop_Cancel() },
    "S413_Transition" to { S413_Transition() },
    "S414_AnimatedVisibility" to { S414_AnimatedVisibility() },
    "S415_Crossfade" to { S415_Crossfade() },
    "S416_AnimatedContent" to { S416_AnimatedContent() },
)

fun NavController.navigateToChapter4() {
    this.navigate(ROUTE_NAME)
}

fun NavGraphBuilder.chapter4Screen(navController: NavHostController) {
    buildNavigation(true, ROUTE_NAME, entrances, START_PAGE) {
        AnimationScreen(navController)
    }
}