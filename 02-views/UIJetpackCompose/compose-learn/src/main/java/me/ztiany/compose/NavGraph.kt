package me.ztiany.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.ztiany.compose.foundation.animation.animationScreen
import me.ztiany.compose.foundation.custom.customScreen
import me.ztiany.compose.foundation.gesture.gestureScreen
import me.ztiany.compose.foundation.layout.layoutScreen
import me.ztiany.compose.foundation.state.stateManagingScreen
import me.ztiany.compose.foundation.widgets.widgetScreen
import me.ztiany.compose.realistic.realisticScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(navController)
        widgetScreen()
        layoutScreen(navController)
        customScreen(navController)
        animationScreen(navController)
        gestureScreen(navController)
        realisticScreen(navController)
        stateManagingScreen(navController)
    }
}