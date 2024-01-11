package me.ztiany.compose.foundation.animation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.buildEntrances
import me.ztiany.compose.facility.widget.buildNavigation
import me.ztiany.compose.foundation.animation.google.GoogleAnimationHome
import me.ztiany.compose.foundation.animation.practice.FavButton
import me.ztiany.compose.foundation.animation.practice.Shimmer

private const val LAYOUT_PAGE = "animation_page"
private const val LAYOUT_INTERNAL_PAGE = "animation_internal_page"

@Composable
private fun AnimationScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(entrances, navController))
}

private val entrances = linkedMapOf<String, @Composable (NavBackStackEntry) -> Unit>(
    //======================================================
    // 高级动画 API
    //======================================================
    //AnimatedVisibility
    "AnimatedVisibility Text 1" to { AnimatedVisibilityText() },
    "AnimatedVisibility Text 2" to { AnimatedVisibilityTextStartWhenEnter() },
    "AnimatedVisibility Specified" to { AnimatedVisibilitySpecialChildren() },
    "AnimatedVisibility Customized" to { AnimatedVisibilityCustomized() },
    //AnimatedContent
    "AnimatedContent Text" to { AnimatedContentText() },
    "AnimatedContent Content" to { AnimatedContentTextContentTransform() },
    "AnimatedContent Size" to { AnimatedContentTextSizeTransform() },
    //CrossFade
    "CrossFadePage" to { CrossFadePage() },
    //Modifier.animateContentSize
    "AnimateContentSizeBox" to { AnimateContentSizeBox() },


    //======================================================
    // 低级动画 API
    //======================================================
    //AnimateXAsState
    "AnimateXAsState" to { AnimateXAsStateExample() },
    //Animatable
    "AnimatableButton" to { AnimatableExample() },
    //transition
    "Transition-SwitchBox" to { SwitchBox() },
    "Transition-Child" to { ChildTransitionSample() },
    "Transition-AV-AC" to { TransitionWithAV_AC() },
    "EncapsulateTransition" to { EncapsulateTransition() },
    "InfiniteTransition" to { InfiniteTransition() },
    //AnimationSpec
    "AnimationSpec" to { AnimationSpecExample() },
    "AnimationSpec-Spring" to { SpringDemo() },
    //AnimationVector
    "AnimationVector" to { AnimationVectorExample() },


    //======================================================
    // Practice
    //======================================================
    "Shimmer" to { Shimmer() },
    "FavButton" to { FavButton() },


    //======================================================
    // Google Animation CodeLab
    //======================================================
    "Google Animation CodeLab" to { GoogleAnimationHome() },
)

fun NavController.navigateToAnimation() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.animationScreen(navController: NavHostController) {
    buildNavigation(LAYOUT_PAGE, entrances, LAYOUT_INTERNAL_PAGE) {
        AnimationScreen(navController)
    }
}