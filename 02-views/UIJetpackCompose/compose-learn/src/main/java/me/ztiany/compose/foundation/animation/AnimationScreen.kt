package me.ztiany.compose.foundation.animation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.foundation.animation.google.GoogleAnimationHome
import me.ztiany.compose.foundation.animation.practice.FavButton
import me.ztiany.compose.foundation.animation.practice.Shimmer

@Composable
fun AnimationScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return entrances.map {
        Entrance(it.key) {
            navController.navigate(it.key)
        }
    }
}

private val entrances = linkedMapOf<String, @Composable () -> Unit>(
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


private const val LAYOUT_PAGE = "animation_page"
private const val LAYOUT_INTERNAL_PAGE = "animation_internal_page"

fun NavController.navigateToAnimation() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.animationScreen(navController: NavHostController) {
    navigation(startDestination = LAYOUT_INTERNAL_PAGE, route = LAYOUT_PAGE) {
        composable(LAYOUT_INTERNAL_PAGE) {
            AnimationScreen(navController)
        }
        for (entrance in entrances) {
            composable(entrance.key) {
                entrance.value()
            }
        }
    }
}

