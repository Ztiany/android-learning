package com.ztiany.androidx.jetpack.livedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.ifNull

class LiveDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                add(R.id.fl_content, LiveDataHomeFragment())
            }
        }
    }

}