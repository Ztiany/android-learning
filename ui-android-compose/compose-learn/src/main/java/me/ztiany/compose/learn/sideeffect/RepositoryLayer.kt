package me.ztiany.compose.learn.sideeffect

import android.media.Image

///////////////////////////////////////////////////////////////////////////
// Result
///////////////////////////////////////////////////////////////////////////

sealed class Result<out T> {

    data object Loading : Result<Nothing>()
    data object Error : Result<Nothing>()
    class Success<T>(val data: T) : Result<T>()

}

///////////////////////////////////////////////////////////////////////////
// ImageRepository
///////////////////////////////////////////////////////////////////////////

interface ImageRepository {

    fun load(url: String): Image?

    companion object {
        fun newDefault(): ImageRepository = ImageRepositoryImpl()
    }

}

private class ImageRepositoryImpl : ImageRepository {

    override fun load(url: String): Image? {
        return null
    }

}

///////////////////////////////////////////////////////////////////////////
// FirebaseAnalytics
///////////////////////////////////////////////////////////////////////////

interface FirebaseAnalytics {

    fun setUserProperty(key: String, value: String)

    companion object {
        fun newDefault(): FirebaseAnalytics = FakeFirebaseAnalytics()
    }

}

private class FakeFirebaseAnalytics : FirebaseAnalytics {
    override fun setUserProperty(key: String, value: String) {

    }
}

data class User(val name: String, val userType: String)
