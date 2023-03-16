package me.ztiany.compose.foundation.tutor

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val TUTOR_PAGE = "tutor_page"

fun NavController.navigateToTutor() {
    this.navigate(TUTOR_PAGE)
}

fun NavGraphBuilder.tutorScreen() {
    composable(route = TUTOR_PAGE) {
        TutorPage()
    }
}