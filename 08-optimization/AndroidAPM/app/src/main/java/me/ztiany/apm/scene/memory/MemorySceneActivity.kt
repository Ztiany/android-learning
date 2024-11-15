package me.ztiany.apm.scene.memory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.MemoryActivityMainBinding

class MemorySceneActivity : AppCompatActivity() {

    private lateinit var binding: MemoryActivityMainBinding

    private val memoryAllocation by lazy {
        MemoryAllocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemoryActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() = with(binding) {
        btnOpenMemoryLeak.setOnClickListener {
            startActivity(Intent(this@MemorySceneActivity, MemoryLeakActivity::class.java))
        }

        btnOpenJavaHeapShake.setOnClickListener {
            startActivity(Intent(this@MemorySceneActivity, MemoryShakeActivity::class.java))
        }

        btnMakeJavaHeapOom.setOnClickListener {
            buildList {
                repeat(1000) {
                    add(ByteArray(1024 * 1024 * 10))
                }
            }
        }

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
    }

}