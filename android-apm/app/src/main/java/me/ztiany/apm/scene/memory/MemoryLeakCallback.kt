package me.ztiany.apm.scene.memory

object MemoryLeakCallbackHolder {

    private var memoryLeakCallbacks: List<MemoryLeakCallback> = mutableListOf()

    fun addMemoryLeakCallback(callback: MemoryLeakCallback) {
        memoryLeakCallbacks += callback
    }

    fun removeMemoryLeakCallback(callback: MemoryLeakCallback) {
        memoryLeakCallbacks -= callback
    }

    fun printMemoryLeakCallbacks() {
        memoryLeakCallbacks.forEach {
            println(it)
        }
    }

}

interface MemoryLeakCallback {

}