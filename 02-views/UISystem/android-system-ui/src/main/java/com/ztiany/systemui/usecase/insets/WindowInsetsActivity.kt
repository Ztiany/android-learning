package com.ztiany.systemui.usecase.insets

import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ztiany.systemui.R
import timber.log.Timber

/**WindowInsets 研究*/
class WindowInsetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_windowinsets)

        //用于控制  WindowInsets
        val controllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        /*
        注意：
            1. 该方法返回分发给视图树的原始 insets
            2. Insets 只有在 view attached 才是可用的
            3. API 20 及以下 永远 返回 false
         */
        window.decorView.post {
            ViewCompat.getRootWindowInsets(window.decorView)?.run {
                Timber.d("displayCutout %s", displayCutout.toString())
                Timber.d("stableInsetBottom %s", stableInsetBottom.toString())
                Timber.d("stableInsetTop %s", stableInsetTop.toString())
                Timber.d("getInsetsIgnoringVisibility %s", getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars()).toString())
                Timber.d("getInsetsIgnoringVisibility %s", getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars()).toString())
            }
        }

    }

}