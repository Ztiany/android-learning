package com.ztiany.androidx.jetpack.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ztiany.androidx.common.ifNull
import com.ztiany.androidx.kotlin.R

class FragmentDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                add(R.id.fl_content, FirstFragment(), FirstFragment::class.toString())
            }
        }
    }

    // https://stackoverflow.com/questions/57837514/androidx-onbackpresseddispatcher-how-to-consume-the-back-button-press
    // If you really want your activity to handle the back press, you can try something like this in your override:
    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            // There's an active callback; let the fragment handle it
            super.onBackPressed()
        } else {
            // Do your activity's back press handling here
            supportFinishAfterTransition()
        }
    }

}