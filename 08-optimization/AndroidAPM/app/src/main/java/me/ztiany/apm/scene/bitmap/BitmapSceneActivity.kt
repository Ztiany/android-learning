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
        setUpView()
    }

    private fun setUpView() {
        binding.btnOpenExcessiveBitmapScene.setOnClickListener {
            startActivity(Intent(this@BitmapSceneActivity, ExcessiveBitmapActivity::class.java))
        }
    }

}