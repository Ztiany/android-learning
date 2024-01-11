package me.ztiany.compose.rwx

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
import me.ztiany.compose.rwx.facility.Entrance
import me.ztiany.compose.rwx.facility.EntranceList
import me.ztiany.compose.rwx.facility.Header
import me.ztiany.compose.rwx.facility.Item
import me.ztiany.compose.rwx.version2.chapter4_anim.navigateToChapter4
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
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "RengWuXian Compose Course") }) },

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
        Header("Chapter 04: Animation"),
        //学习基础组件的使用
        Entrance("Animation Screen") { navController.navigateToChapter4() },
    )
}