package me.ztiany.compose.foundation.custom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

private const val CUSTOM_PAGE = "custom_page"

private const val CUSTOM_INTERNAL_PAGE = "custom_internal_page"

private const val FIRST_BASE_LINE_TO_TOP_PAGE = "first_base_line_to_top_page"
private const val SIMPLE_COLUMN_PAGE = "simple_column_page"
private const val TWO_TEXTS_PAGE = "two_texts_by_row_page"
private const val LOADING_PROGRESS_BAR_PAGE = "loading_progress_bar_page"
private const val DRAW_RED_DOT_PAGE = "draw_red_dot_page"
private const val DRAW_WITH_CACHE_PAGE = "draw_with_cache_page"
private const val WAVE_LOADING_PAGE = "wave_loading_page"

fun NavController.navigateToCustomDrawAndLayout() {
    this.navigate(CUSTOM_PAGE)
}

internal fun NavController.navigateToFirstBaseLineToTop() {
    this.navigate(FIRST_BASE_LINE_TO_TOP_PAGE)
}

internal fun NavController.navigateToSimpleColum() {
    this.navigate(SIMPLE_COLUMN_PAGE)
}

internal fun NavController.navigateToTwoTexts() {
    this.navigate(TWO_TEXTS_PAGE)
}

internal fun NavController.navigateToProgressBar() {
    this.navigate(LOADING_PROGRESS_BAR_PAGE)
}

internal fun NavController.navigateToRedDot() {
    this.navigate(DRAW_RED_DOT_PAGE)
}

internal fun NavController.navigateToDrawWithCache() {
    this.navigate(DRAW_WITH_CACHE_PAGE)
}

internal fun NavController.navigateToWaveLoading() {
    this.navigate(WAVE_LOADING_PAGE)
}

fun NavGraphBuilder.customScreen(navController: NavHostController) {
    navigation(startDestination = CUSTOM_INTERNAL_PAGE, route = CUSTOM_PAGE) {
        composable(CUSTOM_INTERNAL_PAGE) {
            CustomScreen(navController)
        }

        composable(FIRST_BASE_LINE_TO_TOP_PAGE) {
            FirstBaselineToTopExample()
        }

        composable(SIMPLE_COLUMN_PAGE) {
            SimpleColumnExample()
        }

        composable(TWO_TEXTS_PAGE) {
            TwoTextsScreen()
        }
        composable(LOADING_PROGRESS_BAR_PAGE) {
            DrawLoadingProgressBar()
        }

        composable(DRAW_RED_DOT_PAGE) {
            DrawRedDotScreen()
        }

        composable(DRAW_WITH_CACHE_PAGE) {
            DrawWithCacheDemo()
        }

        composable(WAVE_LOADING_PAGE) {
            WaveLoadingDemo()
        }
    }
}