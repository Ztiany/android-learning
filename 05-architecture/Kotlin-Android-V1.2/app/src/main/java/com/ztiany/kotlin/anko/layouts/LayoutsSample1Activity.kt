package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * CustomViewsKt
 */
class LayoutsSample1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*不需要setContentView，在Activity中会自动调用*/
        verticalLayout {
            val name = editText()
            button("Say Hello") {
                onClick { toast("Hello, ${name.text}!") }
            }
            customView {
                onClick {
                    toast("custom view")
                }
            }.lparams {
                topMargin = dip(10)
                bottomMargin = dip(10)
            }
        }

    }
}
