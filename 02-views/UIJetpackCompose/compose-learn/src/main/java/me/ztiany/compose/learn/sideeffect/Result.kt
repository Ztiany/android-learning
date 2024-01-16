package me.ztiany.compose.learn.sideeffect

sealed class Result<out T> {

    object Loading : Result<Nothing>()
    object Error : Result<Nothing>()
    class Success<T>(val data: T) : Result<T>()

}