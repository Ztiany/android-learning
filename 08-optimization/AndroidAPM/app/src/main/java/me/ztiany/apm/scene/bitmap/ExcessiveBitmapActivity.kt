package me.ztiany.apm.scene.bitmap

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.R
import me.ztiany.apm.databinding.BitmapActivityExcessiveSceneBinding

class ExcessiveBitmapActivity : AppCompatActivity() {

    private lateinit var binding: BitmapActivityExcessiveSceneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BitmapActivityExcessiveSceneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() = with(binding) {
        btnShowBitmapXml.setOnClickListener {
            ivBitmapXml.setImageResource(R.drawable.ball)
        }

        btnShowBitmapCode.setOnClickListener {
            flBitmapCode.removeAllViews()
            val imageView = ImageView(this@ExcessiveBitmapActivity)
            imageView.setImageResource(R.drawable.ball)
            flBitmapCode.addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }
    }

}