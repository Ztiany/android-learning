package me.ztiany.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.ztiany.compose.foundation.custom.customScreen
import me.ztiany.compose.foundation.layout.layoutScreen
import me.ztiany.compose.foundation.tutor.tutorScreen
import me.ztiany.compose.foundation.widgets.widgetScreen

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
        tutorScreen()
        widgetScreen()
        layoutScreen(navController)
        customScreen(navController)
    }
}