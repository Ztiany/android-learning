package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.R

/**
 * Fragment使用Layouts
 */
class LayoutsSample4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //继承AnkoComponent可以实现布局预览
        setContentView(R.layout.activity_common)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fl_content, LayoutsSample4Fragment())
                    .commit()
        }
    }


}
