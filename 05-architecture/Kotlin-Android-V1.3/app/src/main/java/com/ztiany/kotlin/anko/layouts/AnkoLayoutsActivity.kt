package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.verticalLayout

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 22:41
 */
class AnkoLayoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            button("sample1") {
                setOnClickListener {
                    startActivity(intentFor<LayoutsSample1Activity>())
                }
                id
            }
            button("sample2") {
                setOnClickListener {
                    startActivity(intentFor<LayoutsSample2Activity>())
                }
            }
            button("sample3") {
                setOnClickListener {
                    startActivity(intentFor<LayoutsSample3Activity>())
                }
            }
            button("sample4") {
                setOnClickListener {
                    startActivity(intentFor<LayoutsSample4Activity>())
                }
            }
        }
    }
}