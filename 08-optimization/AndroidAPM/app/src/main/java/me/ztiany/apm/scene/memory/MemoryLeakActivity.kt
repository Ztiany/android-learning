package me.ztiany.apm.scene.memory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.utils.setDescription

class MemoryLeakActivity : AppCompatActivity(), MemoryLeakCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDescription("I am causing memory leak! exit this activity then you can detect the memory leak.")
        MemoryLeakCallbackHolder.addMemoryLeakCallback(this)
        MemoryLeakCallbackHolder.printMemoryLeakCallbacks()
    }

    override fun onDestroy() {
        super.onDestroy()
        //MemoryLeakCallbackHolder.removeMemoryLeakCallback(this)
        MemoryLeakCallbackHolder.printMemoryLeakCallbacks()
    }

}