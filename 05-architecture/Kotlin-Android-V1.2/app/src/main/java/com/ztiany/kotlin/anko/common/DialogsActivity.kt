package com.ztiany.kotlin.anko.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.ztiany.kotlin.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat

/**
 * 演示AndroidDialogsKt功能
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 19:13
 */
class DialogsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anko_common_dialogs)
    }

    fun toastTest(view: View) {
        toast("Hi there!")
        longToast("Wow, such a duration")
    }

    fun alertsTest(view: View) {
        alert("Hi, I'm Roy", "Have you tried turning it off and on again?") {
            yesButton { toast("Oh…") }
            noButton {}
        }.show()
    }

    fun alertsCustomTest(view: View) {
        alert {
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView("Enter your name")
                    editText("ABC")
                }
            }
        }.show()
    }

    fun alertsCompatTest(view: View) {
        alert(Appcompat, "Some text message").show()
    }

    fun selectorsTest(view: View) {
        val countries = listOf("Russia", "USA", "Japan", "Australia")
        selector("Where are you from?", countries) { _, i ->
            toast("So you're living in ${countries[i]}, right?")
        }
    }

    fun progressTest(view: View) {
        val dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
        dialog.show()

    }

}