package com.ztiany.androidx.kotlin.ktx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.common.ifNull

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-09 17:51
 */
class KtxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                replace(R.id.fl_content, KtxViewFragment(), KtxViewFragment::class.qualifiedName)
            }
        }
    }

}