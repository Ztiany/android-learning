package com.ztiany.systemui.usecase.cutout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R
import com.ztiany.systemui.utils.SystemBarCompat

class CutoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutout)

        //SystemWindowCompat.setFullScreen(this)
        //SystemWindowCompat.displayInNotch(this)
        SystemBarCompat.setTransparentSystemBarViaViewFlags(this)
        //SystemWindowCompat.hideStatusAndNavigationBar(this)
        //SystemWindowCompat.setTranslucentSystemBar(this, true, true)
    }

}