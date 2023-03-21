package me.ztiany.compose.foundation.layout

import androidx.compose.material.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

private const val LAYOUT_PAGE = "layout_page"

private const val LAYOUT_INTERNAL_PAGE = "layout_internal_page"

private const val COLUMN_ROW_PAGE = "column_row_page"
private const val CONSTRAINS_LAYOUT_PAGE_1 = "constrains_layout_page_1"
private const val CONSTRAINS_LAYOUT_PAGE_2 = "constrains_layout_page_2"
private const val CONSTRAINS_LAYOUT_PAGE_3 = "constrains_layout_page_3"

private const val Scaffold_PAGE = "scaffold_page"
private const val SURFACE_PAGE = "surface_page"
private const val MODAL_BOTTOM_SHEET_LAYOUT_PAGE = "modal_bottom_sheet_layout_page"

fun NavController.navigateToLayouts() {
    this.navigate(LAYOUT_PAGE)
}

internal fun NavController.navigateToArtistCard() {
    this.navigate(COLUMN_ROW_PAGE)
}

internal fun NavController.navigateToConstrains1() {
    this.navigate(CONSTRAINS_LAYOUT_PAGE_1)
}

internal fun NavController.navigateToConstrains2() {
    this.navigate(CONSTRAINS_LAYOUT_PAGE_2)
}

internal fun NavController.navigateToConstrains3() {
    this.navigate(CONSTRAINS_LAYOUT_PAGE_3)
}
internal fun NavController.navigateToScaffold() {
    this.navigate(Scaffold_PAGE)
}

internal fun NavController.navigateToSurface() {
    this.navigate(SURFACE_PAGE)
}

internal fun NavController.navigateToBottomSheet() {
    this.navigate(MODAL_BOTTOM_SHEET_LAYOUT_PAGE)
}

fun NavGraphBuilder.layoutScreen(navController: NavHostController) {
    navigation(startDestination = LAYOUT_INTERNAL_PAGE, route = LAYOUT_PAGE) {
        composable(LAYOUT_INTERNAL_PAGE) {
            LayoutsScreen(navController)
        }

        composable(COLUMN_ROW_PAGE) {
            ArtistCard()
        }

        composable(CONSTRAINS_LAYOUT_PAGE_1) {
            ConstraintLayoutDemo()
        }
        composable(CONSTRAINS_LAYOUT_PAGE_2) {
            QuotesDemo()
        }
        composable(CONSTRAINS_LAYOUT_PAGE_3) {
            UserPortraitDemo()
        }

        composable(Scaffold_PAGE) {
            Text(text = "TODO")
        }

        composable(SURFACE_PAGE) {
            Text(text = "TODO")
        }

        composable(MODAL_BOTTOM_SHEET_LAYOUT_PAGE) {
            Text(text = "TODO")
        }

    }
}