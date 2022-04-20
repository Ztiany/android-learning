package com.ztiany.systemui.larges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R
import timber.log.Timber

class LargestWidthLimitDpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ratio)
        window.decorView.post {
            Timber.d("onCreate: size = %d x %d", window.decorView.width, window.decorView.height)
        }
    }

}