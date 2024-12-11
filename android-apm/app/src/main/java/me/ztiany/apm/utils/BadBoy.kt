package me.ztiany.apm.utils

import timber.log.Timber

fun doBusyWork() {
    var int = 3
    repeat(10 * 10000000) {
        int += 1
    }
    Timber.d("int = $int")
}