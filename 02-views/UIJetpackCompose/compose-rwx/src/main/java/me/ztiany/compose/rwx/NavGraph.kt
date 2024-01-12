package me.ztiany.compose.rwx

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.ztiany.compose.rwx.version2.chapter4_anim.chapter4Screen
import me.ztiany.compose.rwx.version2.chapter5_modifier.chapter5Screen

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
        chapter4Screen(navController)
        chapter5Screen(navController)
    }
}