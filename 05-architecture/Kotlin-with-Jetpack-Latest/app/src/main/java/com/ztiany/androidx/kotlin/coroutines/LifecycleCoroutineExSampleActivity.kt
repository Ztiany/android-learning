package com.ztiany.androidx.kotlin.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-12 21:48
 */
class LifecycleCoroutineExSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun demoThenAsync() {
        GlobalScope.asyncWithLifecycle(this, Dispatchers.IO) {
            delay(5000) // 模拟耗时的网络请求
            1
        } thenAsync {
            it + 2
        } then {
            Toast.makeText(this@LifecycleCoroutineExSampleActivity, "the result is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun demoThen() {
        GlobalScope.asyncWithLifecycle(this, Dispatchers.IO) {
            delay(5000) // 模拟耗时的网络请求
            1
        } then {
            Toast.makeText(this@LifecycleCoroutineExSampleActivity, "the result is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun demoBind() {
        GlobalScope.bindWithLifecycle(this) {

            GlobalScope.async(Dispatchers.Main) {
                val deferred1 = async(Dispatchers.Default) {
                    delay(1000)
                    1
                }

                val deferred2 = async(Dispatchers.Default) {
                    delay(1500)
                    2
                }

                val result = deferred1.await() + deferred2.await()
                Toast.makeText(this@LifecycleCoroutineExSampleActivity, "the result is $result", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun demoAwaitOrNull() {
        val deferred = GlobalScope.asyncWithLifecycle(this, Dispatchers.IO) {
            delay(5000) // 模拟耗时的网络请求
            1
        }
        GlobalScope.asyncWithLifecycle(this, Dispatchers.Main) {
            val result = deferred.awaitOrNull(4000)
            Toast.makeText(this@LifecycleCoroutineExSampleActivity, "the result is $result", Toast.LENGTH_SHORT).show()
        }
    }

    private fun demoAsync() {
        GlobalScope.asyncWithLifecycle(this, Dispatchers.Main) {
            delay(1000)
            Toast.makeText(this@LifecycleCoroutineExSampleActivity, "hi, this must use 'Dispatchers.Main'", Toast.LENGTH_SHORT).show()
        }
    }

}