package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI

/**
 * Fragment使用Layouts
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 22:32
 */
class LayoutsSample4Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return UI {
            verticalLayout {
                val name = editText()
                button("Say Hello fragment") {
                    onClick { toast("Hello, ${name.text}!") }
                }
                customView {
                    onClick {
                        toast("custom view in fragment")
                    }
                }.lparams {
                    topMargin = dip(10)
                    bottomMargin = dip(10)
                }
            }
        }.view
    }
}