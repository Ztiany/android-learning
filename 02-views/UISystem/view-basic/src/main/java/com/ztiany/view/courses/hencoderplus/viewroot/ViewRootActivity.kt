package com.ztiany.view.courses.hencoderplus.viewroot

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.view.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-04 15:02
 */
class ViewRootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hencoder_viewroot_main)
    }

    fun changeImmediately(view: View) {
        startActivity(Intent(this, ChangeImmediatelyActivity::class.java))
    }

    fun changeDelay(view: View) {
        startActivity(Intent(this, ChangeDelayActivity::class.java))
    }

    fun changeWhenClick(view: View) {
        startActivity(Intent(this, ChangeWithClickActivity::class.java))
    }

    fun changeWrapWhenClick(view: View) {
        startActivity(Intent(this, ChangeWrapWithClickActivity::class.java))
    }

    fun requestBeforeChangeWrapWhenClick(view: View) {
        startActivity(Intent(this, RequestBeforeChangeWrapWithClickActivity::class.java))
    }

}