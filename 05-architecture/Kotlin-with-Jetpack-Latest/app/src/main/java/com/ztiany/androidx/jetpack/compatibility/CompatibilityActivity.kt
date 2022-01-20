package com.ztiany.androidx.jetpack.compatibility

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.androidx.kotlin.R

class CompatibilityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compatibility)
    }

}