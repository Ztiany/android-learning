package me.ztiany.apm.scene.memory

class MemoryAllocation {


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