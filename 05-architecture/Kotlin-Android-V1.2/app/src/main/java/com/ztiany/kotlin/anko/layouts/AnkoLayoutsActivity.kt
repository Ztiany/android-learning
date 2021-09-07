package com.ztiany.kotlin.anko.layouts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
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
                onClick {
                    startActivity(intentFor<LayoutsSample1Activity>())
                }
            }
            button("sample2") {
                onClick {
                    startActivity(intentFor<LayoutsSample2Activity>())
                }
            }
            button("sample3") {
                onClick {
                    startActivity(intentFor<LayoutsSample3Activity>())
                }
            }
            button("sample4") {
                onClick {
                    startActivity(intentFor<LayoutsSample4Activity>())
                }
            }
        }
    }
}