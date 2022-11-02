package com.ztiany.systemui.cutout

import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.systemui.R
import com.ztiany.systemui.utils.SystemUICompat

private const val TAG = "CutoutActivity"

class CutoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cutout)

        //SystemWindowCompat.setFullScreen(this)
        //SystemWindowCompat.displayInNotch(this)
       SystemUICompat.setTransparentSystemBarViaViewFlags(this)
        //SystemWindowCompat.hideStatusAndNavigationBar(this)
        //SystemWindowCompat.setTranslucentSystemBar(this, true, true)
        window.decorView.post {
            Log.d(TAG, window.decorView.rootWindowInsets.displayCutout.toString())
            Log.d(TAG, window.decorView.rootWindowInsets.stableInsetBottom.toString())
            Log.d(TAG, window.decorView.rootWindowInsets.stableInsetTop.toString())
            Log.d(TAG, window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars()).toString())
            Log.d(TAG, window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).toString())
        }
    }

}