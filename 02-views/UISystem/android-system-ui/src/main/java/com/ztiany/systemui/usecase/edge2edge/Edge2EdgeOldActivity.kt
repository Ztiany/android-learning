package com.ztiany.systemui.usecase.edge2edge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.ztiany.systemui.R
import timber.log.Timber

class Edge2EdgeOldActivity : AppCompatActivity(R.layout.activity_edge_2_edge) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //step 1
        //Build.VERSION.SDK_INT < 30，通过 SystemUIFlag
        //Build.VERSION.SDK_INT >= 30，通过 WindowInsets，参考 https://android.googlesource.com/platform/frameworks/base/+/master/core/java/com/android/internal/policy/PhoneWindow.java
        WindowCompat.setDecorFitsSystemWindows(window, false)

        //step 2
        /*
        You can use the WindowInsetsController API directly, but we strongly recommend using the support library WindowInsetsControllerCompat where possible.
        You can use WindowInsetsControllerCompat API instead of theme.xml to control the status bar content color.
        To do so, use the setAppearanceLightNavigationBars() function, passing in true (changing the foreground color of the navigation to light colored) or false (reverting to the default color).
         */
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        //windowInsetsController?.hide(WindowInsetsCompat.Type.statusBars())

        //step 3：根据需求，调整与 WindowInsets 的交叠区域
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edge_to_edge_root)) { view, windowInsets ->
            val insetsGestures = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            val insetsBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            Timber.d("systemGestures $insetsGestures")
            Timber.d("insetsBars $insetsBars")
            // Apply the insets as padding to the view. Here we're setting all of the
            // dimensions, but apply as appropriate to your layout. You could also
            // update the views margin if more appropriate.
             view.updatePadding(insetsBars.left, insetsBars.top, insetsBars.right, insetsBars.bottom)
            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

}