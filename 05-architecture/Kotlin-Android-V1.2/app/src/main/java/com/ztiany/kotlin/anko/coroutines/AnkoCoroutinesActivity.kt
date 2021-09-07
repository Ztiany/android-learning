package com.ztiany.kotlin.anko.coroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 23:02
 */
class AnkoCoroutinesActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var frameLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout = verticalLayout {
            textView {

            }
            button("click me") {
                onClick(CommonPool) {
                    info("CommonPool " + Thread.currentThread())
                    launch(UI) {
                        info("UI " + Thread.currentThread())
                        toast("I am a Button")
                    }
                }
            }
        }
    }

}