package me.ztiany.apm.scene.memory

class LargeObject {

    private val largeObject = ByteArray(1024 * 1024 * 10)

    fun getLargeObject(): ByteArray {
        return largeObject
    }

    fun release() {
        // release the large object
    }

}