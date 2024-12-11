package me.ztiany.apm.scene.bitmap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.BitmapActivityMainBinding

class BitmapSceneActivity : AppCompatActivity() {

    private lateinit var binding: BitmapActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BitmapActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
    }

    private fun setupButtons() = with(binding) {
        btnOpenExcessiveBitmapScene.setOnClickListener {
            startActivity(Intent(this@BitmapSceneActivity, ExcessiveBitmapSceneActivity::class.java))
        }

        btnOpenBitmapMonitorScene.setOnClickListener {
            startActivity(Intent(this@BitmapSceneActivity, BitmapMonitorSceneActivity::class.java))
        }
    }

}