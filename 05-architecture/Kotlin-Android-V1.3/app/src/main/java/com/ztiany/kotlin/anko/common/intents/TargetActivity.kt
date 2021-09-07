package com.ztiany.kotlin.anko.common.intents

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 18:32
 */
class TargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = intent.getStringExtra("id")
        setContentView(textView)
    }

}