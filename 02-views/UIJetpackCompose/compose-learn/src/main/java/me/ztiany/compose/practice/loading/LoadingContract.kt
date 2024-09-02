package me.ztiany.compose.practice.loading

sealed interface LoadingIntent

data object Query : LoadingIntent

data class Update(val open: Boolean) : LoadingIntent