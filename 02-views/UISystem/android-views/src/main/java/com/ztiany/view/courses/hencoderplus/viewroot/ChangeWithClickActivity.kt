package com.ztiany.view.courses.hencoderplus.viewroot

import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.view.R
import kotlin.concurrent.thread

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-04 15:05
 */
class ChangeWithClickActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hencoder_viewroot_exact)
        val tv = findViewById<TextView>(R.id.hen_coder_view_root_tv)
        tv.setOnClickListener {
            thread {
                tv.text = "${Thread.currentThread().name}"
            }
        }
    }

}