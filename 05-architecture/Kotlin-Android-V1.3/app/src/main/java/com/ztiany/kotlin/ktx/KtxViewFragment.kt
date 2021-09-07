package com.ztiany.kotlin.ktx

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.toBitmap
import com.ztiany.kotlin.coroutines.ex.launchUI
import kotlinx.coroutines.async
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-09 17:55
 */
class KtxViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL

                val iv = imageView()

                textView("I am a Text") {
                    padding = dip(10)
                    backgroundColor = Color.RED
                    textColor = Color.BLACK
                    textSize = 20F

                    setOnClickListener {
                        launchUI {
                            val result = async {
                                val toBitmap: Bitmap = this@textView.toBitmap()
                                toBitmap
                            }
                            iv.setImageBitmap(result.await())
                        }
                    }
                }
            }
        }.view
    }

}