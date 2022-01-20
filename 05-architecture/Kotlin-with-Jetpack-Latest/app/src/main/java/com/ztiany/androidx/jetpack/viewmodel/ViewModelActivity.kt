package com.ztiany.androidx.jetpack.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.common.ifNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                add(R.id.fl_content, ViewModelHomeFragment(), ViewModelHomeFragment::class.qualifiedName)
            }
        }
    }

}