package me.ztiany.apm.scene.memory

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import me.ztiany.apm.utils.setDescription

class MemoryShakeActivity : AppCompatActivity(), MemoryLeakCallback {

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // 创造内存抖动
            buildList {
                repeat(10) {
                    add(LargeObject())
                }
            }
            sendEmptyMessageDelayed(0, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDescription("I am causing memory shake!")
        handler.sendEmptyMessage(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}