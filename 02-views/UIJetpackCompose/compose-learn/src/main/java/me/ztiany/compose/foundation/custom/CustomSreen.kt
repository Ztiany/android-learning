package me.ztiany.compose.foundation.custom

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import me.ztiany.compose.facilities.widget.Entrance
import me.ztiany.compose.facilities.widget.EntranceList

@Composable
fun CustomScreen(navController: NavHostController) {
    EntranceList(entranceList = buildEntrances(navController))
}

private fun buildEntrances(navController: NavController): List<Entrance> {
    return listOf(
        //drawBehind 修饰符 / drawWithContent 修饰符
        Entrance("画红点") { navController.navigateToRedDot() },
        //Canvas 组件
        Entrance("Canvas 组件") { navController.navigateToProgressBar() },
        //drawWithCache 修饰符
        Entrance("DrawWithCache") { navController.navigateToDrawWithCache() },
        //layout 修饰符
        Entrance("Layout 修饰符") { navController.navigateToFirstBaseLineToTop() },
        //Layout 组件
        Entrance("Layout 组件") { navController.navigateToSimpleColum() },
        //IntrinsicSize / SubcomposeLayout
        Entrance("IntrinsicSize") { navController.navigateToTwoTexts() },
        //实践 + 原始 Canvas API
        Entrance("WaveLoading") { navController.navigateToWaveLoading() },
    )
}