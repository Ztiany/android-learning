package com.ztiany.androidnew

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.ztiany.androidnew.aop.StatisticEvent
import com.ztiany.androidnew.ui.BlankFragment
import com.ztiany.androidnew.ui.main.MainFragment
import com.ztiany.androidnew.ui.page2.Page2Fragment
import com.ztiany.androidnew.ui.page3.Page3Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .add(BlankFragment(), "BlankFragment")
                    .commit()
        }

    }

    @StatisticEvent("open2")
    fun open2(view: View) {
        supportFragmentManager.beginTransaction()
                .addToBackStack("Page 2")
                .add(R.id.container, Page2Fragment())
                .apply {
                    supportFragmentManager.findFragmentById(R.id.container)?.let {
                        hide(it)
                    }
                }
                .commit()
    }

    private fun currentVisibleFragment(): List<Fragment> {
        supportFragmentManager.fragments.forEach {
            Log.d("MainActivity", "${it::class.java.simpleName} it.isHidden =  ${it.isHidden}  isAdded = ${it.isAdded} isVisible = ${it.isVisible}  isResumed = ${it.isResumed}")
        }
        return supportFragmentManager.fragments.filter {
            it.isVisible
        }
    }

    @StatisticEvent("open3")
    fun open3(view: View) {
        supportFragmentManager.beginTransaction()
                .addToBackStack("Page 3")
                .add(R.id.container, Page3Fragment())
                .apply {
                    supportFragmentManager.findFragmentById(R.id.container)?.let {
                        hide(it)
                    }
                }
                .commit()
    }

}
