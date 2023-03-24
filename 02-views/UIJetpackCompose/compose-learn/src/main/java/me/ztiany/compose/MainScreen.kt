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
import me.ztiany.compose.foundation.animation.navigateToAnimation
import me.ztiany.compose.foundation.custom.navigateToCustomDrawAndLayout
import me.ztiany.compose.foundation.layout.navigateToLayouts
import me.ztiany.compose.foundation.state.navigateToStateManaging
import me.ztiany.compose.foundation.widgets.navigateToWidgets
import me.ztiany.compose.realistic.navigateToRealistic

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
        topBar = { TopAppBar(title = { Text(text = "JetpackCompose Learning") }) },

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
        //学习基础组件的使用
        Entrance("Basic Widgets") { navController.navigateToWidgets() },
        //学习基础布局的使用
        Entrance("Basic Layout") { navController.navigateToLayouts() },
        //学习自定义布局与绘制 API
        Entrance("Custom Draw&Layout") { navController.navigateToCustomDrawAndLayout() },
        //动画
        Entrance("Animation API") { navController.navigateToAnimation() },
        //手势
        Entrance("Gesture API") { navController.navigateToAnimation() },
        //状态管理
        Entrance("State Management") { navController.navigateToStateManaging() },
        //功能性组件
        Entrance("CompositionLocal") { navController.navigateToAnimation() },
        Entrance("Side Effect") { navController.navigateToAnimation() },
        //真实案例
        Entrance("Realistic Pages") { navController.navigateToRealistic() },
    )
}