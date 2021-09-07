package com.ztiany.kotlin.coroutines

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import java.util.*
import kotlin.coroutines.CoroutineContext


/**
 *https://github.com/dmytrodanylyk/coroutine-recipes
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-05-04 17:41
 */
class LaunchCoroutinesFragment : Fragment(), AnkoLogger {

    private lateinit var mText: TextView

    private val dataProvider = DataProvider()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return UI {

            verticalLayout {

                orientation = LinearLayout.VERTICAL

                textView {
                    mText = this@textView
                }

                button {
                    text = "开始"
                    setOnClickListener {
                        loadData()
                    }
                }.lparams {
                    topMargin = context.dip(20)
                }
            }
        }.view
    }


    private fun loadData() = GlobalScope.launch(Dispatchers.Main) {
        debug("loading")
        mText.text = "loading"
        val result = dataProvider.loadData() // non ui thread, suspend until finished
        showText(result) // ui thread
        debug("end")
    }

    private fun showText(result: String) {
        mText.text = result
    }

    class DataProvider(private val context: CoroutineContext = Dispatchers.Default) {

        /**
         * Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns the result.
         */
        suspend fun loadData(): String = withContext(context) {
            delay(2000) // imitate long running operation
            "Data is available: ${Random().nextInt()}"
        }

    }


}