package me.ztiany.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import me.ztiany.compose.facility.widget.Entrance
import me.ztiany.compose.facility.widget.EntranceList
import me.ztiany.compose.facility.widget.Header
import me.ztiany.compose.facility.widget.Item
import me.ztiany.compose.foundation.animation.navigateToAnimation
import me.ztiany.compose.foundation.custom.navigateToCustomDrawAndLayout
import me.ztiany.compose.foundation.gesture.navigateToGesture
import me.ztiany.compose.foundation.layout.navigateToLayouts
import me.ztiany.compose.foundation.modifiers.navigateToModifier
import me.ztiany.compose.foundation.state.navigateToStateManaging
import me.ztiany.compose.foundation.widgets.navigateToWidgets
import me.ztiany.compose.realistic.navigateToRealistic
import timber.log.Timber

const val MAIN_SCREEN = "main_screen"

fun NavGraphBuilder.mainScreen(navController: NavHostController) {
    composable(route = MAIN_SCREEN) {
        MainScreen(navController)
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        Timber.d("enter MainScreen")
    }
    Timber.d("compose/recompose MainScreen")
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

private fun buildEntrances(navController: NavController): List<Item> {
    return listOf(
        //UI 组件
        Header("UI Widgets"),
        //学习基础组件的使用
        Entrance("Basic Widgets") { navController.navigateToWidgets() },
        //学习基础布局的使用
        Entrance("Basic Layout") { navController.navigateToLayouts() },
        //学习自定义布局与绘制 API
        Entrance("Custom Draw&Layout") { navController.navigateToCustomDrawAndLayout() },


        //UI 交互
        Header("UI Interaction"),
        //动画
        Entrance("Animation API") { navController.navigateToAnimation() },
        //手势
        Entrance("Gesture API") { navController.navigateToGesture() },


        //功能性组件
        Header("Functionality"),
        //Modifier 原理探索
        Entrance("Modifier Exploring") { navController.navigateToModifier() },
        //状态管理
        Entrance("State Management") { navController.navigateToStateManaging() },
        //功能性组件
        Entrance("CompositionLocal") { navController.navigateToAnimation() },
        //副作用 API
        Entrance("Side Effect") { navController.navigateToAnimation() },


        //真实案例
        Header("Real Samples"),
        Entrance("Realistic Pages") { navController.navigateToRealistic() },
    )
}