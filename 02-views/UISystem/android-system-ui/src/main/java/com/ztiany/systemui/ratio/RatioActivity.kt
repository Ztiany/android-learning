package com.ztiany.systemui.ratio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R
import timber.log.Timber

private const val TAG = "RatioActivity"

class RatioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ratio)
        window.decorView.post {
            Timber.d("onCreate: size = %d x %d", window.decorView.width, window.decorView.height)
        }
    }

}