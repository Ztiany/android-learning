package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.ifNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfficialFlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                add(R.id.fl_content, OfficialFlowHomeFragment(), OfficialFlowHomeFragment::class.java.canonicalName)
            }
        }
    }

}