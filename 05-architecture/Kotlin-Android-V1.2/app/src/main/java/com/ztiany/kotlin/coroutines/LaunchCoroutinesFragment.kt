package com.ztiany.kotlin.coroutines

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

private val job: Job = Job()

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
    private val uiContext: CoroutineContext = UI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return UI {

            verticalLayout {
                orientation = LinearLayout.VERTICAL

                textView {
                    mText = this@textView
                }

                button {
                    text = "开始"
                    onClick {
                        loadData()
                    }
                }.lparams {
                    topMargin = context.dip(20)
                }

            }
        }.view
    }


    private fun loadData() = launch(uiContext, parent = job) {
        debug("loading")
        mText.text = "loading"
        val result = dataProvider.loadData() // non ui thread, suspend until finished
        showText(result) // ui thread
        debug("end")
    }

    private fun showText(result: String) {
        mText.text = result
    }

    class DataProvider(private val context: CoroutineContext = CommonPool) {

        /**
         * Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns the result.
         */
        suspend fun loadData(): String = withContext(context) {
            delay(2, TimeUnit.SECONDS) // imitate long running operation
            "Data is available: ${Random().nextInt()}"
        }

    }


}