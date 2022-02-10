package com.ztiany.view.courses.hencoderplus

import android.content.res.Resources
import android.util.TypedValue

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-03 15:26
 */
val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = this.toFloat().dp