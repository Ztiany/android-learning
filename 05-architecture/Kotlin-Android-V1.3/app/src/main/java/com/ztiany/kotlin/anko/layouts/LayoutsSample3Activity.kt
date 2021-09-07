package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*

class LayoutsSample3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUI().setContentView(this)
    }

    //继承AnkoComponent可以实现布局预览(AndroidStudio2.4以上)
    class ActivityUI : AnkoComponent<LayoutsSample3Activity> {
        override fun createView(ui: AnkoContext<LayoutsSample3Activity>) = with(ui) {
            verticalLayout {
                val name = editText()
                button("Say Hello") {
                    setOnClickListener { ctx.toast("Hello, ${name.text}!") }
                }
            }
        }

    }
}
