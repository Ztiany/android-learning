package com.ztiany.androidnew.ui.page2

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-12-27 11:44
 */
class Page2Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Page2Fragment", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(context!!)
        textView.text = "page 2"
        textView.setTextColor(Color.RED)
        return textView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("Page2Fragment", "onHiddenChanged = {$hidden}")
    }

}