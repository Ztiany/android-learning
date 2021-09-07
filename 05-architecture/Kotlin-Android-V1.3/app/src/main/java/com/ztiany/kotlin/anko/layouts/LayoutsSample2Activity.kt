package com.ztiany.kotlin.anko.layouts

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.R
import kotlinx.android.synthetic.main.activity_anko_layouts2.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast

class LayoutsSample2Activity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_anko_layouts2)

        anko_layouts_tv_name.text = "Click me"
        //onClick已经执行在协程中了，通过参数可以指定协程上下文
        anko_layouts_tv_name.setOnClickListener {
            //启动异步
            val async = GlobalScope.async {
                delay(3000)
                "ABC"
            }
            //在UI线程中等待结果
            GlobalScope.launch(Dispatchers.Main) {
                toast(" I am TextView ${async.await()}")
            }
        }

    }
}
