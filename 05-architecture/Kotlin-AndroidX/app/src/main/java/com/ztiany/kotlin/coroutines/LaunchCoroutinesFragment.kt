package com.ztiany.kotlin.coroutines

import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


/**
 *https://github.com/dmytrodanylyk/coroutine-recipes
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-05-04 17:41
 */
class LaunchCoroutinesFragment : Fragment() {

    private lateinit var mText: TextView

    private val dataProvider = DataProvider()

    private fun loadData() = GlobalScope.launch(Dispatchers.Main) {
        mText.text = "loading"
        val result = dataProvider.loadData() // non ui thread, suspend until finished
        showText(result) // ui thread
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