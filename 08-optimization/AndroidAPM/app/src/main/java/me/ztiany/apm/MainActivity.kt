package me.ztiany.apm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.ActivityMainBinding
import me.ztiany.apm.scene.bitmap.BitmapSceneActivity
import me.ztiany.apm.scene.memory.MemorySceneActivity
import me.ztiany.apm.scene.throttle.ThrottleClickActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpOperationView()
        setUpNavigationView()
    }

    private fun setUpOperationView() = with(binding) {
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
            App.jniBridge.hookMemoryAllocation()
        }

        btnDumpMemory.setOnClickListener {
            Timber.d("dump memory info")
            App.jniBridge.dumpMemoryAllocationInfo()
        }
    }

    private fun setUpNavigationView() = with(binding) {
        btnEnterMemoryTrackerScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, MemorySceneActivity::class.java))
        }
        btnEnterBitmapScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, BitmapSceneActivity::class.java))
        }
        btnEnterThrottleScene.setOnClickListener {
            startActivity(Intent(this@MainActivity, ThrottleClickActivity::class.java))
        }
    }

}