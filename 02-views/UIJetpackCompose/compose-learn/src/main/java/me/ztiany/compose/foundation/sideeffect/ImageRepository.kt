package me.ztiany.compose.foundation.sideeffect

import android.media.Image

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

