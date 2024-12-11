package me.ztiany.apm.utils

import java.util.Locale

fun Long.getFormatSize(): String {
    var memory = this * 1.0F
    var unit = "b"
    if (memory > 1024) {
        memory /= 1024
        unit = "Kb"
    }
    if (memory > 1024) {
        memory /= 1024
        unit = "Mb"
    }
    if (memory > 1024) {
        memory /= 1024
        unit = "Gb"
    }
    return String.format(Locale.getDefault(), "%.0f %s", memory, unit)
}