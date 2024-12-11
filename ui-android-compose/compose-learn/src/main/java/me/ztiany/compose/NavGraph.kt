package me.ztiany.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.ztiany.compose.learn.animation.animationScreen
import me.ztiany.compose.learn.custom.customScreen
import me.ztiany.compose.learn.dialog.dialogScreen
import me.ztiany.compose.learn.gesture.gestureScreen
import me.ztiany.compose.learn.layout.layoutScreen
import me.ztiany.compose.learn.modifier.modifierScreen
import me.ztiany.compose.learn.sideeffect.sideEffectScreen
import me.ztiany.compose.learn.state.stateManagingScreen
import me.ztiany.compose.learn.theme.themeScreen
import me.ztiany.compose.learn.widgets.widgetScreen
import me.ztiany.compose.platform.platformScreen
import me.ztiany.compose.practice.practiceScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(navController)
        widgetScreen(navController)
        dialogScreen(navController)
        layoutScreen(navController)
        customScreen(navController)
        animationScreen(navController)
        gestureScreen(navController)
        stateManagingScreen(navController)
        modifierScreen(navController)
        sideEffectScreen(navController)
        practiceScreen(navController)
        platformScreen(navController)
        themeScreen(navController)
    }
}