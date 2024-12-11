package com.ztiany.androidx.kotlin.coroutines.research

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.androidx.kotlin.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-05-04 17:33
 */
class CoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_common)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fl_content, LaunchCoroutinesFragment())
                    .commit()
        }

    }

}