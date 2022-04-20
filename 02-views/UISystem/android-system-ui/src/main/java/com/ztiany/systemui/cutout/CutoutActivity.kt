package com.ztiany.systemui.cutout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R

class CutoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutout)

        SystemWindowCompat.setFullScreen(this)
        SystemWindowCompat.displayInNotch(this)
        SystemWindowCompat.setTransparentSystemBarViaViewFlags(this)
        SystemWindowCompat.addSystemUiVisibilityFlags(this, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}