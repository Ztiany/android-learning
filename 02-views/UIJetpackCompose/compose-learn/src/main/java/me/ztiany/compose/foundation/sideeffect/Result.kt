package me.ztiany.compose.foundation.sideeffect

sealed class Result<out T> {

    object Loading : Result<Nothing>()
    object Error : Result<Nothing>()
    class Success<T>(val data: T) : Result<T>()

}