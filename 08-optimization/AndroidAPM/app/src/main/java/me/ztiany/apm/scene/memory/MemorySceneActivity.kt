package me.ztiany.apm.scene.memory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.MemoryActivityTrackerBinding

class MemorySceneActivity : AppCompatActivity() {

    private lateinit var binding: MemoryActivityTrackerBinding

    private val memoryAllocation by lazy {
        MemoryAllocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemoryActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() = with(binding) {
        btnAllocateJavaMemory.setOnClickListener {
            memoryAllocation.allocateJavaMemory()
        }

        btnFreeJavaMemory.setOnClickListener {
            memoryAllocation.freeJavaMemory()
        }

        btnAllocateNativeMemory.setOnClickListener {
            memoryAllocation.allocateNativeMemory()
        }

        btnFreeNativeMemory.setOnClickListener {
            memoryAllocation.freeNativeMemory()
        }

        btnMakeJavaHeapOom.setOnClickListener {
            buildList {
                repeat(1000) {
                    add(ByteArray(1024 * 1024 * 10))
                }
            }
        }
    }

    companion object {
        fun start(starter: Activity) {
            starter.startActivity(Intent(starter, MemorySceneActivity::class.java))
        }
    }

}