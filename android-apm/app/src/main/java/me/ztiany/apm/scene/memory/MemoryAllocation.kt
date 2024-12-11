package me.ztiany.apm.scene.memory

class MemoryAllocation {

    companion object {
        init {
            System.loadLibrary("memory_allocation")
        }
    }

    private var javaMemory: ByteArray? = null

    fun allocateJavaMemory() {
        javaMemory = ByteArray(1024 * 1024 * 10)
    }

    fun freeJavaMemory() {
        javaMemory = null
        System.gc()
    }

    external fun allocateNativeMemory()

    external fun freeNativeMemory()

}