package com.ztiany.kotlin.extension

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-08-27 17:27
 */
class ExtensionSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extension_sample)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.extension_fl, ExtensionSampleFragment.newInstance(User("Zhan", "Tianyou")))
                    .commit()
        }
    }

}