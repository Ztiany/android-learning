@file:JvmName("SizeEx")

package me.ztiany.wifi.kit.sys

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

fun Context.dp(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
}

fun Context.dp(value: Int): Int {
    return dp(value.toFloat()).roundToInt()
}

fun Context.sp(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)
}