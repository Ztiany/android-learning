package com.ztiany.kotlin.ktx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.R
import com.ztiany.kotlin.ifNull

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
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_content, KtxViewFragment(), KtxViewFragment::class.qualifiedName)
                    .commit()
        }
    }


}