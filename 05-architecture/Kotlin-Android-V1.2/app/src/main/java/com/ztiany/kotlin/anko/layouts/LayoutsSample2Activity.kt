package com.ztiany.kotlin.anko.layouts

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.R
import kotlinx.android.synthetic.main.activity_anko_layouts2.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class LayoutsSample2Activity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_anko_layouts2)

        anko_layouts_tv_name.text = "Click me"
        //onClick已经执行在协程中了，通过参数可以指定协程上下文
        anko_layouts_tv_name.onClick {
            //启动异步
            val async = async(CommonPool) {
                delay(3000)
                "ABC"
            }
            //在UI线程中等待结果
            launch(UI) {
                toast(" I am TextView ${async.await()}")
            }
        }

    }
}
