package com.ztiany.androidx.common

/**
 *@author Ztiany
 */
fun <T> T?.ifNull(action: () -> Unit) {
    if (this == null) {
        action.invoke()
    }
}