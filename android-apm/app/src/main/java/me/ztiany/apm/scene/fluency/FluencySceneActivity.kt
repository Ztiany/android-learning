package me.ztiany.apm.scene.fluency

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.databinding.FluencyActivityMainBinding

class FluencySceneActivity : AppCompatActivity() {

    private lateinit var binding: FluencyActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FluencyActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        btnOpenListScene.setOnClickListener {
            startActivity(Intent(this@FluencySceneActivity, ListFluencySceneActivity::class.java))
        }
    }

}