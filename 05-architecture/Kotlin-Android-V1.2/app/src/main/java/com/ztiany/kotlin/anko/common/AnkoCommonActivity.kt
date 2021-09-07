package com.ztiany.kotlin.anko.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*

/**
 * 演示：DimensionsKt，HelpersKt，UIKt 的功能
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 23:55
 */
class AnkoCommonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = linearLayout {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            textView("Title")
            editText {
                hint = "enter your name"
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                button("ok")
                button("cancel")
            }
            textView("End....")
        }.applyRecursively {//UIKt提供的遍历View树的方法，遍历顺序为深度优先
            view: View ->
            println(view)
            when (view) {
                is EditText -> {
                    view.textSize = px2sp(20)
                    view.textColor = 0xff0000.opaque
                }
                is TextView -> {
                    view.textSize = px2sp(12)
                    view.textColor = 0x99.gray.opaque
                }
            }
        }
        setContentView(view)
    }


}