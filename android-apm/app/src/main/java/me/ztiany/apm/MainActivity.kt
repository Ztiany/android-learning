package me.ztiany.apm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.ActivityMainBinding
import me.ztiany.apm.scene.bitmap.BitmapSceneActivity
import me.ztiany.apm.scene.fluency.FluencySceneActivity
import me.ztiany.apm.scene.memory.MemorySceneActivity
import me.ztiany.apm.scene.throttle.ThrottleClickActivity
import me.ztiany.apm.utils.dumpSystemInfo
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupOperationView()
        setupNavigationView()
    }

    private fun setupOperationView() = with(binding) {
        btnDumpApmInfo.setOnClickListener {
            Timber.d("=============== system info ===============")
            dumpSystemInfo()
            Timber.d("=============== gc monitor ===============")
            App.gcMonitor.dump()
            Timber.d("=============== lmk monitor ===============")
            App.lmkMonitor.dump()
        }

        btnStartMemoryTracker.setOnClickListener {
            Timber.d("start memory tracker")
            App.memoryTracker.hookMemoryAllocation()
        }

        btnDumpMemory.setOnClickListener {
            Timber.d("dump memory info")
            App.memoryTracker.dumpMemoryAllocationInfo()
        }

        btnStartBitmapMonitor.setOnClickListener {
            Timber.d("start bitmap monitor")
            App.bitmapMonitor.start()
        }
    }

    private fun setupNavigationView() = with(binding) {
        btnEnterMemoryTrackerScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, MemorySceneActivity::class.java))
        }
        btnEnterBitmapScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, BitmapSceneActivity::class.java))
        }
        btnEnterThrottleScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, ThrottleClickActivity::class.java))
        }
        btnEnterFluencyScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, FluencySceneActivity::class.java))
        }
    }

}