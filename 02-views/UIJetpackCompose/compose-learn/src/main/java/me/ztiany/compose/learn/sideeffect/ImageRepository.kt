package me.ztiany.compose.learn.sideeffect

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

