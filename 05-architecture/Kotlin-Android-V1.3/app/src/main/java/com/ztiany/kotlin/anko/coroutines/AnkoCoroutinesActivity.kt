package com.ztiany.kotlin.anko.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 23:02
 */
class AnkoCoroutinesActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var frameLayout: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout = verticalLayout {
            textView {
                text = "AnkoCoroutinesActivity"
            }
            button("click me") {
                setOnClickListener {
                    GlobalScope.launch {
                        info("CommonPool " + Thread.currentThread())
                        GlobalScope.launch(Dispatchers.Main) {
                            info("UI " + Thread.currentThread())
                            toast("I am a Button")
                        }
                    }
                }
            }
        }
    }

}