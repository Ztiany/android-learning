package me.ztiany.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList
import me.ztiany.compose.foundation.layout.navigateToLayouts
import me.ztiany.compose.foundation.tutor.navigateToTutor
import me.ztiany.compose.foundation.widgets.navigateToWidgets

const val MAIN_SCREEN = "main_screen"

fun NavGraphBuilder.mainScreen(navController: NavHostController) {
    composable(route = MAIN_SCREEN) {
        MainScreen(navController)
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    /*
      A surface container using the 'background' color from the theme
        1. Surface 是 MD 里面的一个概念。
        2. Surface 具有一些特性，比如背景是黑色，里面的文字就会自动变为白色。
        3. Surface 不是必须的。
 */
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "JetpackCompose 学习") }) },

        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val entranceList = remember {
                    buildEntrances(navController)
                }
                EntranceList(entranceList = entranceList)
            }
        }
    )
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return listOf(
        Entrance("入门示例") { navController.navigateToTutor() },
        Entrance("基础组件") { navController.navigateToWidgets() },
        Entrance("基础布局") { navController.navigateToLayouts() },
        Entrance("CompositionLocal") { navController.navigateToTutor() },
        Entrance("Side Effect") { navController.navigateToTutor() },
        Entrance("Custom Draw") { navController.navigateToTutor() },
        Entrance("Custom Layout") { navController.navigateToTutor() },
        Entrance("Animation API") { navController.navigateToTutor() },
        Entrance("Gesture API") { navController.navigateToTutor() },
    )
}