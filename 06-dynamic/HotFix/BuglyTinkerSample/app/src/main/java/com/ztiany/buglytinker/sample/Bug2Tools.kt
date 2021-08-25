package com.ztiany.buglytinker.sample

import android.content.Context
import android.widget.Toast

/**
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-04-20 15:52
 */

fun doBug2(context: Context) {
    val a = 8
//    val b = 0
    val b = 4
    val c = a / b
    Toast.makeText(context, "kotlin c = $c", Toast.LENGTH_SHORT).show()
}