package me.ztiany.compose.foundation.layout

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

private const val LAYOUT_PAGE = "layout_page"

private const val BOX_PAGE = "box_page"
private const val SURFACE_PAGE = "surface_page"
private const val CENTER_PAGE = "center_page"
private const val COLUMN_ROW_PAGE = "column_row_page"
private const val CONSTRAINS_LAYOUT_PAGE = "constrains_layout_page"
private const val MODAL_BOTTOM_SHEET_LAYOUT_PAGE = "modal_bottom_sheet_layout_page"
private const val Scaffold_1 = "scaffold_1"

fun NavController.navigateToLayouts() {
    this.navigate(LAYOUT_PAGE)
}

fun NavGraphBuilder.layoutScreen() {
    navigation(startDestination = BOX_PAGE, route = LAYOUT_PAGE) {
        composable(BOX_PAGE) {

        }
        composable(SURFACE_PAGE) {

        }
        composable(CENTER_PAGE) {

        }
        composable(COLUMN_ROW_PAGE) {

        }
        composable(CONSTRAINS_LAYOUT_PAGE) {

        }
        composable(MODAL_BOTTOM_SHEET_LAYOUT_PAGE) {

        }
        composable(Scaffold_1) {

        }
    }
}