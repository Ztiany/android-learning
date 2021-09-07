package com.ztiany.kotlin.anko.common.intents

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ztiany.kotlin.R
import org.jetbrains.anko.*

/**
 * 演示Anko中IntentsKt中的方法
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 18:31
 */
class RawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anko_common_intents_raw)
    }

    fun openOne(view: View) {
        val intent = Intent(this, TargetActivity::class.java)
        intent.putExtra("id", 5)
        intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    fun openTwo(view: View) {
        // intentFor方法，用于构建Intent
        startActivity(intentFor<TargetActivity>("id" to 5).singleTop())
    }

    fun makeCallTest(view: View) {
        makeCall("18817013387")
    }

    fun sendSMSTest(view: View) {
        sendSMS("18817013387", "I love you")
    }

    fun browseTest(view: View) {
        browse("www.qq.com")
    }

    fun shareTest(view: View) {
        share("I am Android", "Android Share")
    }

    fun emailTest(view: View) {
        email("1169654504@qq.com", "Android Email", " this is an email for test anko")
    }
}