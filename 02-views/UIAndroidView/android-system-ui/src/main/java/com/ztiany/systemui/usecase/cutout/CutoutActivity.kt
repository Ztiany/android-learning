package com.ztiany.systemui.usecase.cutout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R
import com.ztiany.systemui.utils.SystemBarCompat

class CutoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutout)
        SystemBarCompat.displayInNotch(this)
    }

}